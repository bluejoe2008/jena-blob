package jenablob.io;

import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;

import org.springframework.core.io.InputStreamSource;

/**
 * @author bluejoe2008@gmail.com
 */
public class InputStreamBlob extends AbstractBlob implements Blob
{
	private InputStreamSource _source;

	public InputStreamBlob(InputStreamSource source) throws IOException
	{
		this(source, -1);
		super.syncLength();
	}

	public InputStreamBlob(InputStreamSource source, int length) throws IOException
	{
		this(source, (long) length);
	}

	public InputStreamBlob(InputStreamSource source, long length) throws IOException
	{
		_source = source;
		super.setLength(length);
		super.syncMark();
		super.syncDigest();
	}

	protected InputStream getInputStream() throws IOException
	{
		return _source.getInputStream();
	}
}
