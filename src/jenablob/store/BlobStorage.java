package jenablob.store;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author bluejoe2008@gmail.com
 */
public interface BlobStorage
{
	void delete(String handle) throws IOException;

	BlobWriter getNamedWriter(String clientName);

	InputStream open(String handle, long length, String digest) throws IOException;
}