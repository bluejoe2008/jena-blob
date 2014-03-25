package jenablob;

import java.io.IOException;

import jenablob.store.BlobStorage;

/**
 * @author bluejoe2008@gmail.com
 */
public interface Blob
{
	public void bindBlobStorage(BlobStorage blobStorage);

	public String getDigest();

	public byte[] getMark();

	public byte[] getMark(int count);

	public byte[] getMark(int startIndex, int endIndex);

	/**
	 * tells if this blob stream is ready to read
	 * e.g. blob object which is loaded from files (RDF files, TDB...) always returns false
	 * @return
	 */
	public boolean isReady();

	public long length();

	public <T> T read(InputStreamConsumer<T> consumer) throws IOException;

	public byte[] readBytes() throws IOException;

	public String readString() throws IOException;
}