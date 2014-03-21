package jenablob.fn;

import jenablob.Blob;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase1;

/**
 * @author bluejoe2008@gmail.com
 */
@FunctionURI("http://bluejoe.cn/jenablob#isBlob")
public class IsBlob extends FunctionBase1
{

	@Override
	public NodeValue exec(NodeValue arg0)
	{
		Node n = arg0.asNode();
		return NodeValue.makeBoolean(n.getLiteralValue() instanceof Blob);
	}

}
