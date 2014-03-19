package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils
{
	public static String stream2String(InputStream is) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		org.apache.log4j.lf5.util.StreamUtils.copy(is, baos);
		String text = new String(baos.toByteArray());
		baos.close();
		return text;
	}
}
