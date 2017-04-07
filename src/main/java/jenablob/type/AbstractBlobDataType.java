package jenablob.type;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jenablob.Blob;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.graph.impl.LiteralLabel;

/**
 * @author bluejoe2008@gmail.com
 */
public abstract class AbstractBlobDataType extends BaseDatatype
{
	public AbstractBlobDataType(String uri)
	{
		super(uri);
	}

	public abstract Blob fromProperties(StringMapWrapper properties) throws IOException;

	@Override
	public boolean isEqual(LiteralLabel value1, LiteralLabel value2)
	{
		return value1.getDatatype() == value2.getDatatype() && value1.getLexicalForm().equals(value2.getLexicalForm());
	}

	@Override
	public boolean isValidValue(Object valueForm)
	{
		return true;
	}

	@Override
	public Object parse(String lexicalForm) throws DatatypeFormatException
	{
		try
		{
			StringMapWrapper map = unwrap(lexicalForm);
			Blob o = fromProperties(map);
			Logger.getLogger(this.getClass()).debug(String.format("parsed %s from: %s", o, lexicalForm));
			return o;
		}
		catch (Exception e)
		{
			throw new DatatypeFormatException(lexicalForm, this, e.getMessage());
		}
	}

	@Override
	public String unparse(Object value)
	{
		StringMapWrapper properties = new StringMapWrapper();
		writeProperties((Blob) value, properties);
		String lexicalForm = wrap(properties);
		Logger.getLogger(this.getClass()).debug(String.format("formated %s as: %s", value, lexicalForm));
		return lexicalForm;
	}

	private StringMapWrapper unwrap(String text)
	{
		Map<String, String> map = new HashMap<String, String>();
		for (String s : text.split(","))
		{
			String[] pair = s.split(":");
			map.put(pair[0], (String) pair[1]);
		}

		return new StringMapWrapper(map);
	}

	private String wrap(StringMapWrapper map)
	{
		String buffer = null;

		for (Entry<String, String> en : map.map().entrySet())
		{
			if (buffer != null)
			{
				buffer += ",";
			}
			else
			{
				buffer = "";
			}
			buffer += en.getKey();
			buffer += ":";
			buffer += en.getValue();
		}

		return buffer;
	}

	public abstract void writeProperties(Blob blob, StringMapWrapper properties);
}