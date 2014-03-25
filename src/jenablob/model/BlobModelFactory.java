package jenablob.model;

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
import jenablob.store.BlobStorage;
import jenablob.store.FileSystemBlobStorage;
import jenablob.type.ByteArrayBlobDataType;
import jenablob.type.FileBlobDataType;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import com.hp.hpl.jena.sparql.function.FunctionRegistry;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.store.GraphTDB;

/**
 * @author bluejoe2008@gmail.com
 */
public class BlobModelFactory
{
	private static int _blobMarkChars = 32;

	static
	{
		Logger.getLogger(BlobModelFactory.class).info("initializing jena blob...");

		registerBlobDataTypes(ByteArrayBlobDataType.DATA_TYPE, FileBlobDataType.DATA_TYPE);
		registerFunctions(IsBlob.class, GetLength.class, GetMark.class, GetDigest.class, GetMarkBytes.class,
			DefineVar.class, ToString.class, ToBytes.class);
	}

	private static void registerBlobDataTypes(RDFDatatype... types)
	{
		for (RDFDatatype type : types)
		{
			TypeMapper.getInstance().registerDatatype(type);
		}
	}

	/**
	 * creates a in-memory model without blob persistence
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Model createMemModel()
	{
		return ModelFactory.createDefaultModel();
	}

	/**
	 * creates a in-memory model with blob persistence
	 * 
	 * @param blobStorage
	 * @return
	 * @throws Exception
	 */
	public static Model createMemModel(BlobStorage blobStorage)
	{
		return new ModelCom(new BlobGraphMem(blobStorage));
	}

	public static Model createMemModel(File blobDir)
	{
		return createMemModel(new FileSystemBlobStorage(blobDir));
	}

	public static Model createTDBModel(File dir) throws Exception
	{
		return createTDBModel(dir, new FileSystemBlobStorage(new File(dir, "blob")));
	}

	public static Model createTDBModel(File tdbDir, BlobStorage blobStorage) throws Exception
	{
		Model model = TDBFactory.createDataset(tdbDir.getPath()).getDefaultModel();
		GraphTDB graph = (GraphTDB) model.getGraph();
		return new ModelCom(new BlobGraphTDB((DatasetGraphTDB) graph.getDataset(), graph.getGraphName(), blobStorage));
	}

	public static int getBlobMarkChars()
	{
		return _blobMarkChars;
	}

	private static void registerFunctions(Class... functionClasses)
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
}
