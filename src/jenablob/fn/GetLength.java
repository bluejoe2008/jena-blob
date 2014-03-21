package jenablob.fn;

import jenablob.Blob;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase1;

@FunctionURI("http://bluejoe.cn/jenablob#length")
public class GetLength extends FunctionBase1
{
	@Override
	public NodeValue exec(NodeValue arg0)
	{
		Node n = arg0.asNode();
		Object literalValue = n.getLiteralValue();
		if (literalValue instanceof Blob)
		{
			return NodeValue.makeInteger(((Blob) literalValue).length());
		}
		else
		{
			return NodeValue.nvZERO;
		}
	}

}
