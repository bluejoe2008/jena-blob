package jenablob.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.InputStreamConsumer;
import jenablob.type.FileBlob;
import jenablob.type.FileBlobDataType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * @author bluejoe2008@gmail.com
 */
public class FileSystemBlobStorage implements BlobStorage
{
	private File _rootDir;

	public FileSystemBlobStorage(File rootDir)
	{
		_rootDir = rootDir;
	}

	public void delete(Blob blob) throws IOException
	{
		String handle = ((FileBlob) blob).getFileHandle();
		new File(_rootDir, handle).delete();
		new File(_rootDir, handle.replaceAll("\\.bin", ".md5")).delete();
	}

	public RDFDatatype getBlobDataType()
	{
		return FileBlobDataType.DATA_TYPE;
	}

	public InputStream open(Blob blob) throws IOException
	{
		String digest = blob.getDigest();
		long length = blob.length();
		String handle = ((FileBlob) blob).getFileHandle();

		File file = new File(_rootDir, handle);
		if (file.length() != length)
		{
			throw new FailedToOpenStream(blob);
		}

		File digestFile = new File(_rootDir, handle.replaceAll("\\.bin", "\\.md5"));
		FileInputStream fis = new FileInputStream(digestFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(fis, baos);
		fis.close();
		
		if (!digest.equals(new String(baos.toByteArray())))
		{
			throw new FailedToOpenStream(blob);
		}

		return new FileInputStream(file);
	}

	public Blob translate(Blob blob) throws IOException
	{
		String fileName = String.format("%d.bin", System.currentTimeMillis());
		File file = new File(_rootDir, fileName);
		file.getParentFile().mkdirs();
		final FileOutputStream fos = new FileOutputStream(file);
		blob.read(new InputStreamConsumer<Long>()
		{
			public Long consume(InputStream is) throws IOException
			{
				return IOUtils.copyLarge(is, fos);
			}
		});
		fos.close();

		String digestFileName = fileName.replaceAll("\\.bin", ".md5");
		File digestFile = new File(_rootDir, digestFileName);
		FileInputStream fis = new FileInputStream(file);
		String digest = DigestUtils.md5Hex(fis);
		fis.close();
		FileOutputStream dfos = new FileOutputStream(digestFile);
		dfos.write(digest.getBytes());
		dfos.close();

		return new FileBlob(this, fileName, file.length(), null, digest);
	}
}
