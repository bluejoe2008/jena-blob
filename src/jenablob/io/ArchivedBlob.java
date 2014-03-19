package jenablob.io;

import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.storage.BlobStorageServiceFactory;

public class ArchivedBlob extends AbstractBlob implements Blob
{
	private String _fileHandle;

	public ArchivedBlob(String fileHandle)
	{
		super();
		_fileHandle = fileHandle;
	}

	public String getFileHandle()
	{
		return _fileHandle;
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return BlobStorageServiceFactory.getInstance().open(_fileHandle);
	}
}
