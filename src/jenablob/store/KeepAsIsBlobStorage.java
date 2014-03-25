package jenablob.store;

import java.io.IOException;

import jenablob.Blob;

import com.hp.hpl.jena.datatypes.RDFDatatype;
/**
 * @author bluejoe2008@gmail.com
 */
public class KeepAsIsBlobStorage implements BlobStorage
{
	public void delete(Blob blob) throws IOException
	{
	}

	public RDFDatatype getBlobDataType()
	{
		return null;
	}

	public Blob translate(Blob blob) throws IOException
	{
		return blob;
	}
}
