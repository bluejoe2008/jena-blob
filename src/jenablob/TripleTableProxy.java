package jenablob;

import java.io.IOException;
import java.io.InputStream;

import jenablob.io.ArchivedBlob;
import jenablob.io.BlobDataType;
import jenablob.io.InputStreamBlob;
import jenablob.store.BlobWriter;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.tdb.nodetable.NodeTupleTable;
import com.hp.hpl.jena.tdb.store.TripleTable;

public class TripleTableProxy extends TripleTable
{
	private BlobWriter _blobWriter;

	public TripleTableProxy(BlobWriter blobWriter, NodeTupleTable nodeTupleTable, TripleTable tripleTable)
	{
		super(nodeTupleTable.getTupleTable().getIndexes(), nodeTupleTable.getNodeTable(), nodeTupleTable.getPolicy());
		_blobWriter = blobWriter;
	}

	public TripleTableProxy(BlobWriter blobWriter, TripleTable tripleTable)
	{
		this(blobWriter, tripleTable.getNodeTupleTable(), tripleTable);
	}

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
					InputStreamBlob isb = (InputStreamBlob) v;
					String handle = isb.read(new InputStreamConsumer<String>()
					{
						public String consume(InputStream is) throws IOException
						{
							return _blobWriter.save(is);
						}
					});

					ArchivedBlob ab = new ArchivedBlob(handle, isb.length(), isb.getMark(), isb.getDigest());
					o = Node.createLiteral(BlobDataType.BLOB_TYPE.unparse(ab), BlobDataType.BLOB_TYPE);
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return super.add(s, p, o);
	}

	@Override
	public boolean delete(Node s, Node p, Node o)
	{
		if (o != null && o.isLiteral())
		{
			Object v = o.getLiteralValue();
			//BLOB
			if (v instanceof ArchivedBlob)
			{
				try
				{
					ArchivedBlob mb = (ArchivedBlob) v;
					_blobWriter.getBlobStorage().delete(mb.getFileHandle());
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return super.delete(s, p, o);
	}

}
