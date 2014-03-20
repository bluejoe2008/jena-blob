package jenablob.lit;

import java.io.IOException;


public interface Blob
{
	public <T> T read(InputStreamConsumer<T> consumer) throws IOException;

	public String readString() throws IOException;
}