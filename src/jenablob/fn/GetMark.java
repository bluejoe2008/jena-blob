package jenablob.fn;

import java.util.List;

import com.hp.hpl.jena.sparql.expr.NodeValue;

@FunctionURI("http://bluejoe.cn/jenablob#mark")
public class GetMark extends GetMarkBytes
{
	@Override
	public NodeValue exec(List<NodeValue> arg0)
	{
		ObjectNodeValue v = (ObjectNodeValue) super.exec(arg0);
		return NodeValue.makeString(new String((byte[]) v.getRawObject()));
	}
}
