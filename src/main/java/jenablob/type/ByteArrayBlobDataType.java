package jenablob.type;

import java.io.IOException;

import jenablob.Blob;

/**
 * @author bluejoe2008@gmail.com
 */
public class ByteArrayBlobDataType extends AbstractBlobDataType
{
	public static final ByteArrayBlobDataType DATA_TYPE = new ByteArrayBlobDataType();

	public static final String DATA_TYPE_URI = "urn:x-hp-dt:blob-bytes";

	public ByteArrayBlobDataType()
	{
		super(DATA_TYPE_URI);
	}

	@Override
	public Blob fromProperties(StringMapWrapper properties) throws IOException
	{
		return new ByteArrayBlob(null, properties.getBytes("content"), properties.getLong("length"),
				properties.getBytes("mark"), properties.getString("digest"));
	}

	@Override
	public void writeProperties(Blob blob, StringMapWrapper properties)
	{
		ByteArrayBlob ab = (ByteArrayBlob) blob;
		properties.putLong("length", ab.length());
		properties.putBytes("mark", ab.getMark());
		properties.putString("digest", ab.getDigest());
		properties.putBytes("content", ab.getContent());
	}
}
