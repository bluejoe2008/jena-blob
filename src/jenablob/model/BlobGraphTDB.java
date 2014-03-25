package jenablob.model;

import java.util.ArrayList;
import java.util.List;

import jenablob.Blob;
import jenablob.store.BlobStorage;
import util.ClassUtils;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.store.GraphTDB;
import com.hp.hpl.jena.tdb.store.TripleTable;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author bluejoe2008@gmail.com
 */
public class BlobGraphTDB extends GraphTDB
{
	BlobGraph _blobGraph;

	public BlobGraphTDB(DatasetGraphTDB dataset, Node graphName, BlobStorage storage) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException
	{
		super(dataset, graphName);
		_blobGraph = new BlobGraph(storage);

		TDBTripleTableProxy tripleTable = new TDBTripleTableProxy(_blobGraph, (TripleTable) ClassUtils.getFieldValue(
			this, "dsg.tripleTable"));
		ClassUtils.setFieldValue(this, "dsg.tripleTable", tripleTable);
	}

	@Override
	public void clear()
	{
		ExtendedIterator<Triple> it = this.find(Node.ANY, Node.ANY, Node.ANY);
		List<Node> nodes = new ArrayList<Node>();
		while (it.hasNext())
		{
			Node node = it.next().getObject();
			if (node.isLiteral() && node.getLiteralValue() instanceof Blob)
			{
				nodes.add(node);
			}
		}

		super.clear();
		for (Node node : nodes)
		{
			_blobGraph.deleteBlobObject(node);
		}
	}
}
