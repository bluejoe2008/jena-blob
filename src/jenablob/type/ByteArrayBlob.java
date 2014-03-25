package jenablob.type;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.store.BlobStorage;

/**
 * @author bluejoe2008@gmail.com
 */
public class ByteArrayBlob extends AbstractBlob implements Blob
{
	private byte[] _content;

	public ByteArrayBlob(BlobStorage blobStorage, byte[] content, long length, byte[] mark, String digest)
			throws IOException
	{
		super(blobStorage);
		_content = content;
		super.setMark(mark);
		super.setLength(length);
		super.setDigest(digest);
		sync();
	}

	public byte[] getContent()
	{
		return _content;
	}

	protected InputStream getInputStream() throws IOException
	{
		return new ByteArrayInputStream(_content);
	}

	public boolean isReady()
	{
		return _content != null;
	}

	public void setContent(byte[] content)
	{
		_content = content;
	}

}
