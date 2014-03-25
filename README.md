jena-blob
=========

write/read/query BLOBs(binary large objects) in Jena framework

jena-blob enables RDF store to manage both structured data and unstructred data 

visit https://github.com/bluejoe2008/jena-blob/blob/master/test/JenaBlobTest.java for examples

create blob objects in Java
=========
use BlobLiteral.create() to create blob objects:
* public static Literal create(byte[] bytes)
* public static Literal create(File file)
* public static Literal create(InputStreamSource source)
* public static Literal create(String text)


read bolbs
=========

