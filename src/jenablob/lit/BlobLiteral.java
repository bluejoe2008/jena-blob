package jenablob.lit;

import java.io.File;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class BlobLiteral
{
	private static Model _model = ModelFactory.createDefaultModel();

	public static Literal create(byte[] bytes)
	{
		return _model.createTypedLiteral(new InputStreamBlob(new ByteArrayResource(bytes)));
	}

	public static Literal create(File file)
	{
		return _model.createTypedLiteral(new InputStreamBlob(new FileSystemResource(file)));
	}

	public static Literal create(InputStreamSource source)
	{
		return _model.createTypedLiteral(new InputStreamBlob(source));
	}

	public static Literal create(String text)
	{
		return _model.createTypedLiteral(new InputStreamBlob(new ByteArrayResource(text.getBytes())));
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
