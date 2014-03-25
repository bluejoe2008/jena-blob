package jenablob.model;

import java.io.IOException;

import jenablob.Blob;
import jenablob.store.BlobStorage;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.impl.LiteralLabelFactory;

/**
 * @author bluejoe2008@gmail.com
 */
public class BlobGraph
{
	private BlobStorage _blobStorage;

	public BlobGraph(BlobStorage blobStorage)
	{
		_blobStorage = blobStorage;
	}

	public void deleteBlobObject(Node o)
	{
		if (o != null && o.isLiteral())
		{
			Object v = o.getLiteralValue();
			//BLOB
			if (v instanceof Blob)
			{
				try
				{
					_blobStorage.delete((Blob) v);
					Logger.getLogger(this.getClass()).debug(String.format("deleted %s", v));
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	public Node saveBlobObject(Node o)
	{
		if (o != null && o.isLiteral())
		{
			Object v = o.getLiteralValue();
			//BLOB
			if (v instanceof Blob)
			{
				if (((Blob) v).isReady())
				{
					try
					{
						Blob ab = _blobStorage.translate((Blob) v);
						if (ab != v)
						{
							Logger.getLogger(this.getClass()).debug(String.format("saved %s as %s", v, ab));
						}

						return NodeFactory.createLiteral(LiteralLabelFactory.create(ab, "",
							_blobStorage.getBlobDataType()));
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					((Blob) v).bindBlobStorage(_blobStorage);
				}
			}
		}

		return o;
	}
}
