package jenablob.storage;


import java.io.IOException;
import java.io.InputStream;

import jenablob.ModelConfig;

public interface BlobStorageService
{
	InputStream open(String handle) throws IOException;

	String save(ModelConfig cfg, InputStream is) throws IOException;
}
