package jenablob;

import java.io.IOException;
import java.io.InputStream;

public interface Blob
{
	public InputStream getInputStream() throws IOException;

	public String readString() throws IOException;
}