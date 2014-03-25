package jenablob.type;

import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.store.BlobStorage;
import jenablob.store.FileSystemBlobStorage;

/**
 * @author bluejoe2008@gmail.com
 */
public class FileBlob extends AbstractBlob implements Blob
{
	private String _fileHandle;

	public FileBlob(BlobStorage blobStorage, String fileHandle, long length, byte[] mark, String digest)
			throws IOException
	{
		super(blobStorage);
		_fileHandle = fileHandle;
		super.setMark(mark);
		super.setLength(length);
		super.setDigest(digest);
		sync();
	}

	public String getFileHandle()
	{
		return _fileHandle;
	}

	protected InputStream getInputStream() throws IOException
	{
		return ((FileSystemBlobStorage) _blobStorage).open(this);
	}

	public boolean isReady()
	{
		return _blobStorage != null;
	}
}
