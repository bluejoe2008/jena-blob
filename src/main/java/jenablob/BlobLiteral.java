package jenablob;

import java.io.File;
import java.io.IOException;

import jenablob.model.BlobModelFactory;
import jenablob.type.InputStreamBlob;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author bluejoe2008@gmail.com
 */
public class BlobLiteral
{
	private static Model _model = BlobModelFactory.createMemModel();

	public static Literal create(byte[] bytes) throws IOException
	{
		return _model.createTypedLiteral(new InputStreamBlob(new ByteArrayResource(bytes), bytes.length));
	}

	public static Literal create(File file) throws IOException
	{
		return _model.createTypedLiteral(new InputStreamBlob(new FileSystemResource(file), file.length()));
	}

	public static Literal create(InputStreamSource source) throws IOException
	{
		return _model.createTypedLiteral(new InputStreamBlob(source));
	}

	public static Literal create(String text) throws IOException
	{
		return create(text.getBytes());
	}

	public static Blob getBlob(Literal lit)
	{
		return ((Blob) lit.getValue());
	}

	public static boolean isBlobLiteral(Literal lit)
	{
		return lit != null && (lit.getValue() instanceof Blob);
	}
}
