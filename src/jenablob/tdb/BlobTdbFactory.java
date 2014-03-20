package jenablob.tdb;

import java.io.File;

import jenablob.lit.BlobDataType;
import jenablob.store.BlobStorageFactory;
import util.ClassUtils;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.store.TripleTable;
import com.hp.hpl.jena.tdb.transaction.DatasetGraphTransaction;

public class BlobTdbFactory
{
	static
	{
		TypeMapper.getInstance().registerDatatype(BlobDataType.theBlobType);
	}

	public static Model createModel(File dir) throws Exception
	{
		DatasetGraphTransaction transaction = (DatasetGraphTransaction) TDBFactory.createDataset(dir.getPath())
				.asDatasetGraph();
		DatasetGraphTDB graph = ClassUtils.getFieldValue(transaction, "sConn.baseDSG");
		ClassUtils.setFieldValue(graph, "tripleTable", new TripleTableProxy(BlobStorageFactory.getInstance()
				.getNamedWriter(dir.getCanonicalPath()), (TripleTable) ClassUtils.getFieldValue(graph, "tripleTable")));

		return new ModelCom(transaction.getDefaultGraph());
	}

}
