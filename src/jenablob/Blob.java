package jenablob;

import java.io.IOException;

/**
 * @author bluejoe2008@gmail.com
 */
public interface Blob
{
	public String getDigest();

	public byte[] getMark();

	public byte[] getMark(int count);

	public byte[] getMark(int startIndex, int endIndex);

	public long length();

	public <T> T read(InputStreamConsumer<T> consumer) throws IOException;

	public byte[] readBytes() throws IOException;

	public String readString() throws IOException;
}