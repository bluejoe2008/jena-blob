package jenablob.lit;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.graph.impl.LiteralLabel;

public class BlobDataType extends BaseDatatype
{
	public static final BlobDataType theBlobType = new BlobDataType();

	public static final String theTypeURI = "urn:x-hp-dt:blob";

	public BlobDataType()
	{
		super(theTypeURI);
	}

	@Override
	public boolean isEqual(LiteralLabel value1, LiteralLabel value2)
	{
		return value1.getDatatype() == value2.getDatatype() && value1.getValue().equals(value2.getValue());
	}

	@Override
	public Object parse(String lexicalForm) throws DatatypeFormatException
	{
		String handle = lexicalForm;
		return new ArchivedBlob(handle);
	}

	@Override
	public String unparse(Object value)
	{
		if (value instanceof ArchivedBlob)
		{
			ArchivedBlob ab = (ArchivedBlob) value;
			return ab.getFileHandle();
		}

		return "<BLOB>";
	}

}
