package jenablob.type;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
/**
 * @author bluejoe2008@gmail.com
 */
public class StringMapWrapper
{
	Map<String, String> _map = new HashMap<String, String>();

	public StringMapWrapper()
	{
	}

	public StringMapWrapper(Map<String, String> map)
	{
		super();
		_map = map;
	}

	public byte[] getBytes(String key)
	{
		return Base64.decodeBase64(getString(key));
	}

	public long getLong(String key)
	{
		return Long.parseLong(getString(key));
	}

	public String getString(String key)
	{
		return _map.get(key);
	}

	public Map<String, String> map()
	{
		return _map;
	}

	public void putBytes(String key, byte[] bytes)
	{
		putString(key, new String(Base64.encodeBase64((byte[]) bytes)));
	}

	public void putLong(String key, long l)
	{
		putString(key, "" + l);
	}

	public void putString(String key, String text)
	{
		_map.put(key, text);
	}
}
