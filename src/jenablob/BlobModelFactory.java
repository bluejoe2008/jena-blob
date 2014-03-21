package jenablob;

import java.io.File;

import util.ClassUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.store.TripleTable;
import com.hp.hpl.jena.tdb.transaction.DatasetGraphTransaction;

public class BlobModelFactory
{
	static
	{
		Env.init();
	}

	public static Model createMemModel(File dir) throws Exception
	{
		return ModelFactory.createDefaultModel();
	}

	public static Model createTDBModel(File dir) throws Exception
	{
		DatasetGraphTransaction transaction = (DatasetGraphTransaction) TDBFactory.createDataset(dir.getPath())
				.asDatasetGraph();
		DatasetGraphTDB graph = ClassUtils.getFieldValue(transaction, "sConn.baseDSG");
		ClassUtils.setFieldValue(
			graph,
			"tripleTable",
			new TripleTableProxy(Env.getBlobStorage().getNamedWriter(dir.getCanonicalPath()), (TripleTable) ClassUtils
					.getFieldValue(graph, "tripleTable")));

		return new ModelCom(transaction.getDefaultGraph());
	}

}
