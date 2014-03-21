package jenablob.fn;

import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.binding.BindingMap;
import com.hp.hpl.jena.sparql.expr.ExprList;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueString;
import com.hp.hpl.jena.sparql.function.FunctionBase2;
import com.hp.hpl.jena.sparql.function.FunctionEnv;

@FunctionURI("http://bluejoe.cn/jenablob#var")
public class DefineVar extends FunctionBase2
{
	private Binding _binding;

	public NodeValue exec(Binding binding, ExprList args, String uri, FunctionEnv env)
	{
		this._binding = binding;
		return super.exec(binding, args, uri, env);
	}

	@Override
	public NodeValue exec(NodeValue arg0, NodeValue arg1)
	{
		if (arg0 instanceof NodeValueString)
		{
			if (_binding instanceof BindingMap)
			{
				BindingMap map = (BindingMap) _binding;
				NodeValueString nvs0 = (NodeValueString) arg0;
				String varName = nvs0.asString();
				map.add(Var.alloc(varName), arg1.asNode());
			}
		}

		return NodeValue.TRUE;
	}
}
