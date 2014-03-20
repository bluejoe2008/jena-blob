import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jenablob.lit.Blob;
import jenablob.lit.BlobLiteral;
import jenablob.lit.InputStreamBlob;
import jenablob.tdb.BlobTdbFactory;
import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class JenaBlobTest
{
	String _source;

	public JenaBlobTest() throws IOException
	{
		_source = IOUtils.toString(new FileInputStream(new File("e:\\capacity-scheduler.xml")));
	}

	@Test
	public void testBlobLiteral() throws IOException
	{
		Literal lit = BlobLiteral.create(new File("e:\\capacity-scheduler.xml"));
		Assert.assertTrue(lit.getValue() instanceof InputStreamBlob);
		Assert.assertTrue(BlobLiteral.isBlobLiteral((lit)));
		Literal lit2 = BlobLiteral.create(new String("a string"));
		Assert.assertTrue(lit2.getValue() instanceof InputStreamBlob);
		Assert.assertTrue(BlobLiteral.isBlobLiteral((lit2)));
	}

	@Test
	public void testMemModel() throws Exception
	{
		Model memModel = createTestModel();
		doTestModel(memModel);
	}

	@Test
	public void testTdbModel() throws Exception
	{
		//insert BLOB values
		File dir1 = new File("/abc");
		File dir2 = new File("/abc2");

		FileUtils.deleteDirectory(dir1);
		FileUtils.deleteDirectory(dir2);

		Model model1 = BlobTdbFactory.createModel(dir1);
		model1.add(createTestModel());
		doTestModel(model1);

		//update
		model1.listStatements().next().changeObject("new string");
		model1.write(System.out, "TURTLE");
		model1.listStatements().next().changeObject(BlobLiteral.create(new File("e:\\capacity-scheduler.xml")));
		doTestModel(model1);

		model1.close();

		//copying files
		FileUtils.copyDirectory(dir1, dir2);

		model1 = BlobTdbFactory.createModel(dir1);
		model1.removeAll();

		//read
		Model model2 = BlobTdbFactory.createModel(dir2);
		doTestModel(model2);
		//delete
		model2.removeAll();
		model2.close();
	}

	private Model createTestModel() throws IOException
	{
		Model model = ModelFactory.createDefaultModel();
		Literal lit = BlobLiteral.create(new File("e:\\capacity-scheduler.xml"));
		model.add(model.createResource("s"), model.createProperty("p"), lit);
		return model;
	}

	private void doTestModel(Model model) throws IOException
	{
		model.write(System.out, "TURTLE");
		doTestIter(model);
		doTestQuery(model, "select ?s ?p ?o where {?s ?p ?o}");
		//doTestQuery(model, "select ?s ?p ?o where {?s ?p ?o. FILTER(ISBLOB(?o) & length(?o)>10)}");
	}

	private void doTestIter(Model model) throws IOException
	{
		StmtIterator it = model.listStatements();
		Assert.assertTrue(it.hasNext());
		Literal lit = it.next().getLiteral();
		Assert.assertTrue(lit.getValue() instanceof Blob);
		Assert.assertEquals(_source, ((Blob) lit.getValue()).readString());
	}

	private void doTestQuery(Model model, String queryString) throws IOException
	{
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		Assert.assertTrue(results.hasNext());

		QuerySolution soln = results.nextSolution();
		Literal lit = soln.getLiteral("o");
		Assert.assertTrue(BlobLiteral.isBlobLiteral(lit));
		Assert.assertEquals(_source, ((Blob) lit.getValue()).readString());
		qexec.close();
	}
}
