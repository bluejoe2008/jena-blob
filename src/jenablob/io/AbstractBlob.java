package jenablob.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jenablob.Blob;

import org.apache.log4j.lf5.util.StreamUtils;

public abstract class AbstractBlob implements Blob
{
	public String readString() throws IOException
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamUtils.copy(getInputStream(), baos);
		return baos.toString();
	}
}