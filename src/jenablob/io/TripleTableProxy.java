package jenablob.io;


import java.io.IOException;
import java.io.InputStream;

import jenablob.ModelConfig;
import jenablob.storage.BlobStorageServiceFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.tdb.nodetable.NodeTupleTable;
import com.hp.hpl.jena.tdb.store.TripleTable;

public class TripleTableProxy extends TripleTable
{
	private ModelConfig _cfg;

	public boolean add(Node s, Node p, Node o)
	{
		if (o != null && o.isLiteral())
		{
			Object v = o.getLiteralValue();
			//BLOB
			if (v instanceof InputStreamBlob)
			{
				try
				{
					InputStreamBlob mb = (InputStreamBlob) v;
					InputStream is = mb.getInputStream();

					String handle = BlobStorageServiceFactory.getInstance().save(_cfg, is);
					o = Node.createLiteral(BlobDataType.theBlobType.unparse(new ArchivedBlob(handle)),
						BlobDataType.theBlobType);
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return super.add(s, p, o);
	}

	public TripleTableProxy(ModelConfig cfg, TripleTable tripleTable)
	{
		this(cfg, tripleTable.getNodeTupleTable(), tripleTable);
	}

	public TripleTableProxy(ModelConfig cfg, NodeTupleTable nodeTupleTable, TripleTable tripleTable)
	{
		super(nodeTupleTable.getTupleTable().getIndexes(), nodeTupleTable.getNodeTable(), nodeTupleTable.getPolicy());
		_cfg = cfg;
	}

}
