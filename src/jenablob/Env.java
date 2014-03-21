package jenablob;

import java.io.File;

import jenablob.fn.DefineVar;
import jenablob.fn.FunctionURI;
import jenablob.fn.GetDigest;
import jenablob.fn.GetLength;
import jenablob.fn.GetMark;
import jenablob.fn.GetMarkBytes;
import jenablob.fn.IsBlob;
import jenablob.fn.ToBytes;
import jenablob.fn.ToString;
import jenablob.io.BlobDataType;
import jenablob.store.BlobStorage;
import jenablob.store.FileSystemBlobStorage;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.sparql.function.FunctionRegistry;

/**
 * @author bluejoe2008@gmail.com
 */
public class Env
{
	private static int _blobMarkChars = 32;

	private static BlobStorage _blobStorage = new FileSystemBlobStorage(new File("/tmp/blob"));

	static
	{
		Logger.getLogger(Env.class).info("initializing jena blob...");

		TypeMapper.getInstance().registerDatatype(BlobDataType.BLOB_TYPE);
		registerFunctions(IsBlob.class, GetLength.class, GetMark.class, GetDigest.class, GetMarkBytes.class,
			DefineVar.class, ToString.class, ToBytes.class);
	}

	public static int getBlobMarkChars()
	{
		return _blobMarkChars;
	}

	public static BlobStorage getBlobStorage()
	{
		return _blobStorage;
	}

	public static void init()
	{
	}

	public static void registerFunctions(Class... functionClasses)
	{
		for (Class functionClass : functionClasses)
		{
			FunctionRegistry.get().put(((FunctionURI) functionClass.getAnnotation(FunctionURI.class)).value(),
				functionClass);
		}
	}

	public static void setBlobMarkChars(int blobMarkChars)
	{
		_blobMarkChars = blobMarkChars;
	}

	public static void setBlobStorage(BlobStorage blobStorage)
	{
		_blobStorage = blobStorage;
	}
}
