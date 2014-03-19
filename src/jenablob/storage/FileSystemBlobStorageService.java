package jenablob.storage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jenablob.ModelConfig;

import org.apache.log4j.lf5.util.StreamUtils;

public class FileSystemBlobStorageService implements BlobStorageService
{
	private File _rootDir;

	FileSystemBlobStorageService(File rootDir)
	{
		_rootDir = rootDir;
	}

	@Override
	public InputStream open(String handle) throws IOException
	{
		return new FileInputStream(new File(_rootDir, handle));
	}

	@Override
	public String save(ModelConfig cfg, InputStream is) throws IOException
	{
		String fileName = String.format("%s/%d.bin", cfg.getModelName(), System.currentTimeMillis());
		File file = new File(_rootDir, fileName);
		file.getParentFile().mkdirs();
		FileOutputStream os = new FileOutputStream(file);
		StreamUtils.copy(is, os);
		os.close();
		return fileName;
	}
}
