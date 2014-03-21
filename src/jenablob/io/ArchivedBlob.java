package jenablob.io;

import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.Env;

/**
 * @author bluejoe2008@gmail.com
 */
public class ArchivedBlob extends AbstractBlob implements Blob
{
	private String _fileHandle;

	public ArchivedBlob(String fileHandle, long length, byte[] mark, String digest)
	{
		super.setMark(mark);
		super.setLength(length);
		super.setDigest(digest);
		_fileHandle = fileHandle;
	}

	public String getFileHandle()
	{
		return _fileHandle;
	}

	protected InputStream getInputStream() throws IOException
	{
		return Env.getBlobStorage().open(_fileHandle, length(), getDigest());
	}

}
