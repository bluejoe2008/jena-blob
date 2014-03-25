jena-blob
=========

write/read/query BLOBs(binary large objects) in Jena framework

jena-blob enables RDF stores to manage both structured data and unstructred data 

visit https://github.com/bluejoe2008/jena-blob/blob/master/test/JenaBlobTest.java for example usage


create blob objects in Java
=========
a blob object is always accessed via an InputStream interface. BlobLiteral class provides several create() methods to create blob objects:
* public static Literal create(byte[] bytes) : creates a blob from a byte array
* public static Literal create(File file) : creates a blob which contents come from a local file
* public static Literal create(InputStreamSource source) : creates a blob which contents come from a org.springframework.core.io.InputStreamSource
* public static Literal create(String text) : create a blob from a byte array of a string

the following codes illustrate how to add a BLOB as a literal:

		SAMPLE_MODEL = ModelFactory.createDefaultModel();
		Literal lit = BlobLiteral.create(new File("./1.gif"));
		SAMPLE_MODEL.add(SAMPLE_MODEL.createResource("http://s"), SAMPLE_MODEL.createProperty("http://p"), lit);

models and blob storage
=========
As we know, RDF statements can be saved in RDF models. Simply, a blob can be saved in RDF models as a string in certain encoding (BASE64Encoding, for example). However, it is not feasible for large blobs due to large size for RDF statements and long time costs for storing/loading.

A good idea for blob storage is to choose file systems, databases, or other stream storage services. BlobStorage interface is defined to tells where and how to save/load blobs. Several types of BlobStorages are provided in jena-blob:

* ByteArrayBlobStorage : save blobs in byte arrays
* FileSystemBlobStorage : save blobs in local file systems
* KeepAsIsBlobStorage : do not save blobs, only for in-memory blob reading

BlobModelFactory provides several createXXXModel() methods to create models:
* createMemModel() : creates a plain in-memory model, blobs can be added but not be persisted
* createMemModel(BlobStorage blobStorage) : creates an in-memory model, blobs will be persisted in blobStorage
* createMemModel(File blobDir) : creates an in-memory model, blobs will be persisted in local blobDir
* createTDBModel(File dir) : creates a TDB model, blobs will be persisted in local dir/blob
* createTDBModel(File tdbDir, BlobStorage blobStorage) : creates a TDB model, blobs will be persisted in blobStorage

the following codes illustrate blob representation in a ByteArrayBlobStorage:

		<http://s>  <http://p>  "content:R0lGODlhRgAgALP/AP///xAQEBgYGCEhITExMUpKSlpaWmtra3t7e4yMjJycnLW1tcbG
		xt7e3vf39wAAACwAAAAARgAgAEAE/xDISau9OOu923CToQBNwFXOd64aAQJj4wQGILIAIjXcPAKOwoPG4CwQiBfjASLwLIVFJZGQK
		HQVxfPSeBQXBODhIdjiKo3C61Q+TxwHd6jg8UoQtUzDkBg8pCspDmskAj8ZBjJxEgk1B0UUDYcWDgKAbzUOBFUTUg0IjUB0JwlMO4
		tABg8PBwuERgcBQwR8lxMBnBm2ckADvL/AEx5no79hvAcFJA8FAwWqZhgIQgKBPwoDzgcEphkMWAClD6WeA5BGuxqV4Aqr6RdHEpe
		qq4SD6tl5EwwJrwU6KR64wOFL34Iaey74upAgzZt85xi8IkGgCIMAExmOm/BghINuEv8cKBAC8gCqFOAmXLkwg0e7IZw+5gpGsyav
		BdFs6syAYNKGhTt/GXiXIcovUDgQmPkmYegZARlXMCCqS5ipA1UYXIlK4cg9OQ0WLNBypouMArh48DnB7UBODAk8yQg5wOeFiA8M+
		MmzQIWGBAem4UjgZEcBBQHsXkAAaMGSAwrCBKFqgeyKPi80rUKl7pjlBasKcD6hlICyDQugkvBTjNQIVzs6mkRBGcHGSI1Ox25Qxw
		5XCx8UnDMHACMK3ccnOej7ZMk5ClEGOeWQhqkEAlJ6XihcASeARxZGhx/Ag8BroxkUGNBtIM8DDNsiqSaBPGR9CrMBDFiQIMB+01G
		V8oQpWRL8sxwC3IggEXS2OLAeTrwtMIZdhO2ABCGIZbScBnP98huBQYWoUwQAOw==,length:628,digest:5ecb3255a12ef30a9
		d3c3a5554e8021d,mark:R0lGODlhRgAgALP/AP///xAQEBgYGCEhITExMUpKSlo="^^<urn:x-hp-dt:blob-bytes> .
		

the following codes illustrate blob representation in a FileSystemBlobStorage:

		<http://s>  <http://p>  "handle:1395714660338.bin,length:628,digest:5ecb3255a12ef30a9d3c3a5554e8021d
		,mark:R0lGODlhRgAgALP/AP///xAQEBgYGCEhITExMUpKSlo="^^<urn:x-hp-dt.blob-file> .

query on blobs
=========
jena-blob adds some additional information on blob literals which enables blob query:
* length : length of a blob, in long integer
* digest : md5 digest string of a blob, in String (using org.apache.commons.codec.digest.DigestUtils.md5Hex(is))
* mark : the first 32-bytes of the blob, in byte[]

for a Blob object, length()/getDigest()/getMark() methods can be used to retrieve such information.

jena-blob also provides FILTER functions to enable SPARQL query on blobs:
* isBlob() : to determine a literal is a blob or not
* length() : tells the length of a blob
* digest() : tells the digest string of the blob
* mark() : to retrieve the mark of the blob
* mark(size) : to retrieve first size-bytes of mark
* mark(beginning, size) : to retrieve size-bytes of mark, from the beginning of beginning

an example SPARQL query is shown as below:

		
		PREFIX blob: <http://bluejoe.cn/jenablob#>
		select ?s ?p ?o
		(blob:isBlob(?o) as ?v1) (blob:length(?o) as ?v2)	(blob:digest(?o) as ?v3)
		(blob:mark(?o) as ?v4) (blob:mark(?o,6) as ?v5) (blob:mark(?o,0, 6) as ?v6)
		(blob:string(?o) as ?v7) (blob:bytes(?o) as ?v8)
		where {?s ?p ?o. FILTER (blob:isBlob(?o))}
		

