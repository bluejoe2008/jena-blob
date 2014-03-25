package jenablob.store;

import java.io.IOException;

import jenablob.Blob;
import jenablob.type.ByteArrayBlob;
import jenablob.type.ByteArrayBlobDataType;

import com.hp.hpl.jena.datatypes.RDFDatatype;
/**
 * @author bluejoe2008@gmail.com
 */
public class ByteArrayBlobStorage implements BlobStorage
{
	public void delete(Blob blob) throws IOException
	{
	}

	public RDFDatatype getBlobDataType()
	{
		return ByteArrayBlobDataType.DATA_TYPE;
	}

	public Blob translate(Blob blob) throws IOException
	{
		return new ByteArrayBlob(this, blob.readBytes(), blob.length(), blob.getMark(), blob.getDigest());
	}
}
