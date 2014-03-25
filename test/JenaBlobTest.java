import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import jenablob.Blob;
import jenablob.BlobLiteral;
import jenablob.model.BlobModelFactory;
import jenablob.store.ByteArrayBlobStorage;
import jenablob.type.InputStreamBlob;
import junit.framework.Assert;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author bluejoe2008@gmail.com
 */
public class JenaBlobTest
{
	private byte[] INPUT_SOURCE;

	private File INPUT_FILE = new File(".\\1.gif");

	private Model SAMPLE_MODEL;

	public JenaBlobTest() throws IOException
	{
		INPUT_SOURCE = IOUtils.toByteArray(new FileInputStream(INPUT_FILE));
		SAMPLE_MODEL = ModelFactory.createDefaultModel();
		Literal lit = BlobLiteral.create(INPUT_FILE);
		SAMPLE_MODEL.add(SAMPLE_MODEL.createResource("http://s"), SAMPLE_MODEL.createProperty("http://p"), lit);
	}

	@Test
	public void testBlobLiteral() throws IOException
	{
		Literal lit = BlobLiteral.create(INPUT_FILE);
		Assert.assertTrue(lit.getValue() instanceof InputStreamBlob);
		Assert.assertTrue(BlobLiteral.isBlobLiteral((lit)));
		Literal lit2 = BlobLiteral.create(new String("a string"));
		Assert.assertTrue(lit2.getValue() instanceof InputStreamBlob);
		Assert.assertTrue(BlobLiteral.isBlobLiteral((lit2)));
	}

	@Test
	public void testMemModel() throws Exception
	{
		doTestModel(SAMPLE_MODEL, false);
	}

	@Test
	public void testMemFileModel() throws Exception
	{
		Model model = BlobModelFactory.createMemModel(new File("/tmp/blob"));
		model.add(SAMPLE_MODEL);
		doTestModel(model, true);
	}

	@Test
	public void testMemBytesArrayModel() throws Exception
	{
		Model model = BlobModelFactory.createMemModel(new ByteArrayBlobStorage());
		model.add(SAMPLE_MODEL);
		doTestModel(model, true);
	}

	@Test
	public void testTdbModel() throws Exception
	{
		//insert BLOB values
		File dir1 = new File("/abc");
		File dir2 = new File("/abc2");

		FileUtils.deleteDirectory(dir1);
		FileUtils.deleteDirectory(dir2);

		Model model1 = BlobModelFactory.createTDBModel(dir1);
		model1.add(SAMPLE_MODEL);
		doTestModel(model1, true);

		//update
		model1.listStatements().next().changeObject("new string");
		Assert.assertEquals(1, model1.listStatements().toList().size());

		model1.listStatements().next().changeObject(BlobLiteral.create(INPUT_FILE));
		Assert.assertEquals(1, model1.listStatements().toList().size());

		doTestModel(model1, true);

		model1.close();

		//copying files
		FileUtils.copyDirectory(dir1, dir2);

		model1 = BlobModelFactory.createTDBModel(dir1);
		model1.remove(model1.listStatements().next());
		Assert.assertEquals(0, model1.listStatements().toList().size());
		model1.close();
		Assert.assertEquals(0, new File(dir1, "blob").list().length);

		//read
		Model model2 = BlobModelFactory.createTDBModel(dir2);
		doTestModel(model2, true);
		//removeAll
		model2.removeAll();
		Assert.assertEquals(0, model2.listStatements().toList().size());
		model2.close();
		Assert.assertEquals(0, new File(dir2, "blob").list().length);
	}

	private void doTestModel(Model model, boolean reload) throws IOException
	{
		System.out.println("-------------------------------------");
		model.write(System.out, "TURTLE");

		if (reload)
		{
			StringWriter sw = new StringWriter();
			model.write(sw, "TURTLE");
			model.read(new StringReader(sw.getBuffer().toString()), null, "TURTLE");

			System.err.println("-------------------------------------");
			model.write(System.err, "TURTLE");
		}

		doTestStatements(model);
		doTestQuery(model);
		doTestBlobSPARQLQuery(model);
	}

	private void doTestBlobSPARQLQuery(Model model) throws IOException
	{
		String queryString = "PREFIX blob: <http://bluejoe.cn/jenablob#> select ?s ?p ?o (blob:isBlob(?o) as ?v1) (blob:length(?o) as ?v2) (blob:digest(?o) as ?v3) (blob:mark(?o) as ?v4) (blob:mark(?o,6) as ?v5) (blob:mark(?o,0, 6) as ?v6) (blob:string(?o) as ?v7) (blob:bytes(?o) as ?v8) where {?s ?p ?o. FILTER (blob:isBlob(?o))}";
		Query query = QueryFactory.create(queryString, Syntax.syntaxSPARQL_11);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		Assert.assertTrue(results.hasNext());

		QuerySolution soln = results.nextSolution();
		Literal lit = soln.getLiteral("o");
		Assert.assertTrue(BlobLiteral.isBlobLiteral(lit));
		assertBytesEquals(INPUT_SOURCE, ((Blob) lit.getValue()).readBytes());
		Assert.assertEquals(true, soln.getLiteral("v1").getValue());
		Assert.assertEquals(INPUT_SOURCE.length, soln.getLiteral("v2").getValue());
		Assert.assertEquals(DigestUtils.md5Hex(INPUT_SOURCE), soln.getLiteral("v3").getValue());
		Assert.assertEquals("GIF89a", soln.getLiteral("v5").getValue());
		Assert.assertEquals("GIF89a", soln.getLiteral("v6").getValue());
		Assert.assertEquals(new String(INPUT_SOURCE), soln.getLiteral("v7").getValue());
		qexec.close();
	}

	private void doTestStatements(Model model) throws IOException
	{
		StmtIterator it = model.listStatements();
		Assert.assertTrue(it.hasNext());
		Literal lit = it.next().getLiteral();
		Assert.assertTrue(lit.getValue() instanceof Blob);
		assertBytesEquals(INPUT_SOURCE, ((Blob) lit.getValue()).readBytes());
	}

	private void assertBytesEquals(byte[] bs1, byte[] bs2)
	{
		Assert.assertNotNull(bs1);
		Assert.assertNotNull(bs2);
		Assert.assertEquals(bs1.length, bs2.length);
		for (int i = 0; i < bs1.length; i++)
		{
			Assert.assertEquals(bs1[i], bs2[i]);
		}
	}

	private void doTestQuery(Model model) throws IOException
	{
		String queryString = "select ?s ?p ?o where {?s ?p ?o}";
		Query query = QueryFactory.create(queryString, Syntax.syntaxSPARQL_11);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		Assert.assertTrue(results.hasNext());

		QuerySolution soln = results.nextSolution();
		Literal lit = soln.getLiteral("o");
		Assert.assertTrue(BlobLiteral.isBlobLiteral(lit));
		assertBytesEquals(INPUT_SOURCE, ((Blob) lit.getValue()).readBytes());
		qexec.close();
	}
}
