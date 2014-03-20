package jenablob.lit;

import java.io.IOException;
import java.io.InputStream;

import jenablob.store.BlobStorageFactory;

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

	protected InputStream getInputStream() throws IOException
	{
		return BlobStorageFactory.getInstance().open(_fileHandle);
	}
}
