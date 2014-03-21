package jenablob.fn;

import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueString;

public class ObjectNodeValue extends NodeValueString
{
	private Object _rawObject;

	public ObjectNodeValue(Object rawObject)
	{
		super(rawObject == null ? null : rawObject.toString());
		_rawObject = rawObject;
	}

	public Object getRawObject()
	{
		return _rawObject;
	}

	public void setRawObject(Object rawObject)
	{
		_rawObject = rawObject;
	}
}
