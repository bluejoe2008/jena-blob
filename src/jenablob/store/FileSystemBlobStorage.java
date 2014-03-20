package jenablob.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.lf5.util.StreamUtils;

public class FileSystemBlobStorage implements BlobStorage
{
	class FileSystemBlobWriter implements BlobWriter
	{
		private BlobStorage _blobStorage;

		private String _id;

		public FileSystemBlobWriter(BlobStorage blobStorage, String id)
		{
			_blobStorage = blobStorage;
			_id = id;
		}

		public BlobStorage getBlobStorage()
		{
			return _blobStorage;
		}

		public String save(InputStream is) throws IOException
		{
			String fileName = String.format("%s/%d.bin", _id, System.currentTimeMillis());
			File file = new File(_rootDir, fileName);
			file.getParentFile().mkdirs();
			FileOutputStream os = new FileOutputStream(file);
			StreamUtils.copy(is, os);
			os.close();
			return fileName;
		}
	}

	private File _rootDir;

	FileSystemBlobStorage(File rootDir)
	{
		_rootDir = rootDir;
	}

	public void delete(String handle) throws IOException
	{
		new File(_rootDir, handle).delete();
	}

	public BlobWriter getNamedWriter(String clientName)
	{
		String id = DigestUtils.md5Hex(clientName.getBytes());
		return new FileSystemBlobWriter(this, id);
	}

	public InputStream open(String handle) throws IOException
	{
		return new FileInputStream(new File(_rootDir, handle));
	}
}
