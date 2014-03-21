package jenablob.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.graph.impl.LiteralLabel;

/**
 * @author bluejoe2008@gmail.com
 */
public class BlobDataType extends BaseDatatype
{
	public static final BlobDataType BLOB_TYPE = new BlobDataType();

	public static final String BLOB_TYPE_URI = "urn:x-hp-dt:blob";

	public BlobDataType()
	{
		super(BLOB_TYPE_URI);
	}

	@Override
	public boolean isEqual(LiteralLabel value1, LiteralLabel value2)
	{
		return value1.getDatatype() == value2.getDatatype() && value1.getValue().equals(value2.getValue());
	}

	@Override
	public Object parse(String lexicalForm) throws DatatypeFormatException
	{
		try
		{
			byte[] bytes = Base64.decodeBase64(lexicalForm);
			InputStream is = new ByteArrayInputStream(bytes);
			Hessian2Input in = new Hessian2Input(is);
			Object[] obj = (Object[]) in.readObject(null);
			in.close();
			is.close();
			return new ArchivedBlob((String) obj[0], (Long) obj[1], (byte[]) obj[2], (String) obj[3]);
		}
		catch (Exception e)
		{
			throw new DatatypeFormatException(lexicalForm, this, e.getMessage());
		}
	}

	@Override
	public String unparse(Object value)
	{
		if (value instanceof ArchivedBlob)
		{
			ArchivedBlob ab = (ArchivedBlob) value;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Hessian2Output out = new Hessian2Output(os);

			try
			{
				out.writeObject(new Object[] { ab.getFileHandle(), ab.length(), ab.getMark(), ab.getDigest() });
				out.close();
				os.close();
				return new String(Base64.encodeBase64(os.toByteArray()));
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		return "<BLOB>";
	}

}
