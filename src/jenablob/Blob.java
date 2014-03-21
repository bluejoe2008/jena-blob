package jenablob;

import java.io.IOException;

public interface Blob
{
	public String getDigest();

	public long length();

	public byte[] getMark();

	public byte[] getMark(int count);

	public byte[] getMark(int startIndex, int endIndex);

	public <T> T read(InputStreamConsumer<T> consumer) throws IOException;

	public byte[] readBytes() throws IOException;

	public String readString() throws IOException;
}