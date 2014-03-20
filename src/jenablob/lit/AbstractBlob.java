package jenablob.lit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.lf5.util.StreamUtils;

public abstract class AbstractBlob implements Blob
{
	protected abstract InputStream getInputStream() throws IOException;

	public <T> T read(InputStreamConsumer<T> consumer) throws IOException
	{
		InputStream is = null;
		T t = null;
		try
		{
			is = getInputStream();
			t = consumer.consume(is);
		}
		finally
		{
			if (is != null)
			{
				is.close();
			}
		}
		
		return t;
	}

	public String readString() throws IOException
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = getInputStream();
		StreamUtils.copy(is, baos);
		is.close();
		return baos.toString();
	}
}