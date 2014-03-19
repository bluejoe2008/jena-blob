package jenablob.storage;

import java.io.File;

public class BlobStorageServiceFactory
{
	private static BlobStorageService _instance = new FileSystemBlobStorageService(new File("/tmp/blob"));

	public static BlobStorageService getInstance()
	{
		return _instance;
	}

	public static void setInstance(BlobStorageService instance)
	{
		_instance = instance;
	}
}
