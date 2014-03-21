package jenablob.store;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author bluejoe2008@gmail.com
 */
public interface BlobWriter
{
	BlobStorage getBlobStorage();

	String save(InputStream is) throws IOException;
}