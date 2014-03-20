package jenablob.lit;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;

public class InputStreamBlob extends AbstractBlob implements Blob
{
	private InputStreamSource  _source;

	public InputStreamBlob(InputStreamSource  source)
	{
		_source = source;
	}

	protected InputStream getInputStream() throws IOException
	{
		return _source.getInputStream();
	}
}
