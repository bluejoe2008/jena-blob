package jenablob.store;

import java.io.IOException;

import jenablob.Blob;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * @author bluejoe2008@gmail.com
 */
public interface BlobStorage
{
	void delete(Blob blob) throws IOException;

	RDFDatatype getBlobDataType();

	/**
	 * save and translate
	 * 
	 * @param blob
	 * @return
	 * @throws IOException
	 */
	Blob translate(Blob blob) throws IOException;
}