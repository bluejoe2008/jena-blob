package jenablob.fn;

import java.io.IOException;

import jenablob.Blob;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase1;

/**
 * @author bluejoe2008@gmail.com
 */
@FunctionURI("http://bluejoe.cn/jenablob#bytes")
public class ToBytes extends FunctionBase1
{
	@Override
	public NodeValue exec(NodeValue arg0)
	{
		Node n = arg0.asNode();
		Object literalValue = n.getLiteralValue();
		if (literalValue instanceof Blob)
		{
			try
			{
				return new ObjectNodeValue(((Blob) literalValue).readBytes());
			}
			catch (IOException e)
			{
				//e.printStackTrace();
			}
		}

		return new ObjectNodeValue(new byte[0]);
	}
}
