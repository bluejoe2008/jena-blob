package util;

import org.apache.commons.codec.binary.Base64;

public class CodecUtils
{
	public static String encodeBase64(String src)
	{
		return new String(Base64.encodeBase64(src.getBytes()));
	}
	public static String decodeBase64(String src)
	{
		return new String(Base64.decodeBase64(src.getBytes()));
	}
}
