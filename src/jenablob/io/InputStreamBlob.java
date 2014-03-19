package jenablob.io;

import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;

import org.springframework.core.io.InputStreamSource;

public class InputStreamBlob extends AbstractBlob implements Blob
{
	private InputStreamSource  _source;

	public InputStreamBlob(InputStreamSource  source)
	{
		_source = source;
	}

	public InputStream getInputStream() throws IOException
	{
		return _source.getInputStream();
	}
}
