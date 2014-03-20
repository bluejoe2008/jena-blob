package jenablob.store;

import java.io.File;

public class BlobStorageFactory
{
	private static BlobStorage _instance = new FileSystemBlobStorage(new File("/tmp/blob"));

	public static BlobStorage getInstance()
	{
		return _instance;
	}

	public static void setInstance(BlobStorage instance)
	{
		_instance = instance;
	}
}
