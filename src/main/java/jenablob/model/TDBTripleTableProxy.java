package jenablob.model;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.tdb.nodetable.NodeTupleTable;
import com.hp.hpl.jena.tdb.store.TripleTable;

/**
 * @author bluejoe2008@gmail.com
 */
public class TDBTripleTableProxy extends TripleTable
{
	BlobGraph _blobGraph;

	public TDBTripleTableProxy(BlobGraph blobGraph, NodeTupleTable nodeTupleTable, TripleTable tripleTable)
	{
		super(nodeTupleTable.getTupleTable().getIndexes(), nodeTupleTable.getNodeTable(), nodeTupleTable.getPolicy());
		_blobGraph = blobGraph;
	}

	public TDBTripleTableProxy(BlobGraph blobGraph, TripleTable tripleTable)
	{
		this(blobGraph, tripleTable.getNodeTupleTable(), tripleTable);
	}

	@Override
	public boolean add(Node s, Node p, Node o)
	{
		return super.add(s, p, _blobGraph.saveBlobObject(o));
	}

	@Override
	public boolean delete(Node s, Node p, Node o)
	{
		_blobGraph.deleteBlobObject(o);
		return super.delete(s, p, o);
	}
}
