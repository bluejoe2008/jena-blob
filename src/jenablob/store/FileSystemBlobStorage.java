package jenablob.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * @author bluejoe2008@gmail.com
 */
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
			FileOutputStream fos = new FileOutputStream(file);
			IOUtils.copyLarge(is, fos);
			fos.close();

			String digestFileName = fileName.replaceAll("\\.bin", "\\.md5");
			File digestFile = new File(_rootDir, digestFileName);
			FileInputStream fis = new FileInputStream(file);
			String digest = DigestUtils.md5Hex(fis);
			FileOutputStream dfos = new FileOutputStream(digestFile);
			dfos.write(digest.getBytes());
			dfos.close();

			return fileName;
		}
	}

	private File _rootDir;

	public FileSystemBlobStorage(File rootDir)
	{
		_rootDir = rootDir;
	}

	public void delete(String handle) throws IOException
	{
		new File(_rootDir, handle).delete();
		new File(_rootDir, handle.replaceAll("\\.bin", "\\.md5")).delete();
	}

	public BlobWriter getNamedWriter(String clientName)
	{
		String id = DigestUtils.md5Hex(clientName.getBytes());
		return new FileSystemBlobWriter(this, id);
	}

	public InputStream open(String handle, long length, String digest) throws IOException
	{
		//TODO: check md5 digest
		File file = new File(_rootDir, handle);
		if (file.length() != length)
		{
			throw new BadBlobInputStreamException(handle);
		}

		File digestFile = new File(_rootDir, handle.replaceAll("\\.bin", "\\.md5"));
		FileInputStream fis = new FileInputStream(digestFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(fis, baos);
		if (!digest.equals(new String(baos.toByteArray())))
		{
			throw new BadBlobInputStreamException(handle);
		}

		return new FileInputStream(file);
	}
}
