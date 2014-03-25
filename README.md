jena-blob
=========

write/read/query BLOBs(binary large objects) in Jena framework

jena-blob enables RDF store to manage both structured data and unstructred data 

visit https://github.com/bluejoe2008/jena-blob/blob/master/test/JenaBlobTest.java for examples

create blob objects in Java
=========
a blob object is always accessed via an InputStream interface, and BlobLiteral provides several create() methods to create blob objects:
* public static Literal create(byte[] bytes) : creates a blob from a byte array
* public static Literal create(File file) : creates a blob which contents come from a local file
* public static Literal create(InputStreamSource source) : creates a blob which contents come from a spring framework InputStreamSource
* public static Literal create(String text) : create a blob from a byte array of a string

the following codes illustrate how to add a BLOB as a literal:

		SAMPLE_MODEL = ModelFactory.createDefaultModel();
		Literal lit = BlobLiteral.create(INPUT_FILE);
		SAMPLE_MODEL.add(SAMPLE_MODEL.createResource("http://s"), SAMPLE_MODEL.createProperty("http://p"), lit);

models and blob storage
=========
RDF statements can be saved in RDF models. Simply, blob can be saved in RDF models as a string in certain encoding (BASE64Encoding, for example). However, it is obviously unfeasible for large blobs due to both large size for RDF statements and long time cost for loading. A good idea is to save blobs in file systems, databases, or other stream storage services.

RDF statements with blob values can be saved in models, BlobModelFactory provides createXXXModel() methods to create such models:
* createMemModel() : creates a plain in-memory model, blobs can be added but not be persisted
* createMemModel(BlobStorage blobStorage) : creates an in-memory model, blobs will be persisted in blobStorage
* createMemModel(File blobDir) : creates an in-memory model, blobs will be persisted in local blobDir
* createTDBModel(File dir) : creates a TDB model, blobs will be persisted in local dir/blob
* createTDBModel(File tdbDir, BlobStorage blobStorage) : creates a TDB model, blobs will be persisted in blobStorage



read bolbs
=========

