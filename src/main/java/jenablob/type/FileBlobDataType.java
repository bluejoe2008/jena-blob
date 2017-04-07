package jenablob.type;

import java.io.IOException;

import jenablob.Blob;

/**
 * @author bluejoe2008@gmail.com
 */
public class FileBlobDataType extends AbstractBlobDataType
{
	public static final FileBlobDataType DATA_TYPE = new FileBlobDataType();

	public static final String DATA_TYPE_URI = "urn:x-hp-dt.blob-file";

	public FileBlobDataType()
	{
		super(DATA_TYPE_URI);
	}

	@Override
	public Blob fromProperties(StringMapWrapper properties) throws IOException
	{
		return new FileBlob(null, properties.getString("handle"), properties.getLong("length"),
				properties.getBytes("mark"), properties.getString("digest"));
	}

	@Override
	public void writeProperties(Blob blob, StringMapWrapper properties)
	{
		FileBlob ab = (FileBlob) blob;
		properties.putLong("length", ab.length());
		properties.putBytes("mark", ab.getMark());
		properties.putString("digest", ab.getDigest());
		properties.putString("handle", ab.getFileHandle());
	}

}
