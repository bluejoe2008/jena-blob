package jenablob.fn;

import java.util.List;

import jenablob.Blob;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.QueryBuildException;
import com.hp.hpl.jena.sparql.expr.ExprList;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase;
import com.hp.hpl.jena.sparql.util.Utils;

/**
 * @author bluejoe2008@gmail.com
 */
@FunctionURI("http://bluejoe.cn/jenablob#markBytes")
public class GetMarkBytes extends FunctionBase
{
	@Override
	public void checkBuild(String arg0, ExprList arg1)
	{
		if (arg1.size() > 3)
			throw new QueryBuildException("Function '" + Utils.className(this) + "' takes 1~3 arguments");
	}

	@Override
	public NodeValue exec(List<NodeValue> arg0)
	{
		Node n = arg0.get(0).asNode();
		Object literalValue = n.getLiteralValue();
		if (literalValue instanceof Blob)
		{
			if (arg0.size() == 1)
			{
				return new ObjectNodeValue(((Blob) literalValue).getMark());
			}

			if (arg0.size() == 2)
			{
				return new ObjectNodeValue(((Blob) literalValue).getMark(arg0.get(1).getInteger().intValue()));
			}

			if (arg0.size() == 3)
			{
				return new ObjectNodeValue(((Blob) literalValue).getMark(arg0.get(1).getInteger().intValue(),
					arg0.get(2).getInteger().intValue()));
			}
		}

		return new ObjectNodeValue(new byte[0]);
	}

}
