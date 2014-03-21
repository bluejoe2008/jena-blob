package jenablob.store;

import java.io.IOException;
import java.io.InputStream;

public interface BlobWriter
{
	BlobStorage getBlobStorage();

	String save(InputStream is) throws IOException;
}