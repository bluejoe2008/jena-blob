package jenablob.model;

import jenablob.store.BlobStorage;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.mem.GraphMem;

/**
 * @author bluejoe2008@gmail.com
 */
public class BlobGraphMem extends GraphMem
{
	BlobGraph _blobGraph;

	public BlobGraphMem(BlobStorage storage)
	{
		_blobGraph = new BlobGraph(storage);
	}

	@Override
	public void performAdd(Triple t)
	{
		Node o = t.getObject();
		Node o2 = _blobGraph.saveBlobObject(o);
		if (o != o2)
			t = new Triple(t.getSubject(), t.getPredicate(), o2);

		super.performAdd(t);
	}

	@Override
	public void performDelete(Triple t)
	{
		_blobGraph.deleteBlobObject(t.getObject());
		super.performDelete(t);
	}
}
