import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.BlobLiteral;
import jenablob.BlobTdbFactory;
import jenablob.ModelConfig;
import jenablob.io.InputStreamBlob;
import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import util.StreamUtils;

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
		_source = StreamUtils.stream2String(new FileInputStream(new File("e:\\capacity-scheduler.xml")));
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
		memModel.write(System.out, "TURTLE");
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

		Model model1 = BlobTdbFactory.createModel(dir1, new ModelConfig());
		model1.add(createTestModel());
		model1.write(System.out, "TURTLE");
		doTestModel(model1);
		model1.close();

		//copying files
		FileUtils.copyDirectory(dir1, dir2);

		//read
		Model model2 = BlobTdbFactory.createModel(dir2, new ModelConfig());
		model2.write(System.out, "TURTLE");
		doTestModel(model2);
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
		StmtIterator it = model.listStatements();
		Assert.assertTrue(it.hasNext());
		Literal lit = it.next().getLiteral();
		Assert.assertTrue(lit.getValue() instanceof Blob);
		InputStream is = ((Blob) lit.getValue()).getInputStream();
		Assert.assertEquals(_source, StreamUtils.stream2String(is));

		String queryString = "select ?s ?p ?o where {?s ?p ?o}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		Assert.assertTrue(results.hasNext());

		QuerySolution soln = results.nextSolution();
		lit = soln.getLiteral("o");
		Assert.assertTrue(BlobLiteral.isBlobLiteral(lit));
		is = BlobLiteral.getBlob(lit).getInputStream();
		Assert.assertEquals(_source, StreamUtils.stream2String(is));
		is.close();

		qexec.close();
	}
}
