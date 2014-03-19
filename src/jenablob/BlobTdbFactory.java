package jenablob;

import java.io.File;

import jenablob.io.BlobDataType;
import jenablob.io.TripleTableProxy;
import util.ClassUtils;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import com.hp.hpl.jena.sparql.core.DatasetPrefixStorage;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.store.GraphTriplesTDB;
import com.hp.hpl.jena.tdb.store.TripleTable;

public class BlobTdbFactory
{
	static
	{
		TypeMapper.getInstance().registerDatatype(BlobDataType.theBlobType);
	}

	public static Model createModel(File dir, ModelConfig cfg) throws Exception
	{
		GraphTriplesTDB graph = (GraphTriplesTDB) TDBFactory.createDataset(dir.getPath()).getDefaultModel().getGraph();
		GraphTriplesTDB graph2 = new GraphTriplesTDB(graph.getDataset(), new TripleTableProxy(cfg,
				(TripleTable) ClassUtils.getFieldValue(graph, TripleTable.class)),
				(DatasetPrefixStorage) ClassUtils.getFieldValue(graph, DatasetPrefixStorage.class));

		return new ModelCom(graph2);
	}

}
