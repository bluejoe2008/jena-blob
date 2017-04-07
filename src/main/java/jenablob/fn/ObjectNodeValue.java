package jenablob.fn;

import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueString;

/**
 * @author bluejoe2008@gmail.com
 */
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
