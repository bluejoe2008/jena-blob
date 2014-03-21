package jenablob.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jenablob.Blob;
import jenablob.Env;
import jenablob.InputStreamConsumer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public abstract class AbstractBlob implements Blob
{
	public byte[] getMark(int startIndex, int count)
	{
		if (startIndex < 0)
			startIndex = 0;

		if (startIndex > _mark.length)
			return new byte[0];

		if (startIndex + count > _mark.length)
			count = _mark.length - startIndex;

		byte[] trim = new byte[count];
		new ByteArrayInputStream(_mark).read(trim, startIndex, count);
		return trim;
	}

	public byte[] getMark(int count)
	{
		if (count <= 0)
			return new byte[0];

		if (count >= _mark.length)
			return _mark;

		byte[] trim = new byte[count];
		new ByteArrayInputStream(_mark).read(trim, 0, count);
		return trim;
	}

	private long _length;

	public long length()
	{
		return _length;
	}

	protected void syncMark() throws IOException
	{
		setMark(read(new InputStreamConsumer<byte[]>()
		{
			public byte[] consume(InputStream is) throws IOException
			{
				byte[] bytes = new byte[Env.getBlobMarkChars()];
				int read = is.read(bytes);
				if (read > 0)
				{
					byte[] trim = new byte[read];
					new ByteArrayInputStream(bytes).read(trim, 0, read);
					return trim;
				}

				return new byte[0];
			}
		}));
	}

	protected void syncDigest() throws IOException
	{
		setDigest(read(new InputStreamConsumer<String>()
		{
			public String consume(InputStream is) throws IOException
			{
				return DigestUtils.md5Hex(is);
			}
		}));
	}

	protected void setLength(long length)
	{
		_length = length;
	}

	public String getDigest()
	{
		return _digest;
	}

	protected void setDigest(String digest)
	{
		_digest = digest;
	}

	private String _digest = "";

	private byte[] _mark = {};

	public byte[] getMark()
	{
		return _mark;
	}

	public void setMark(byte[] mark)
	{
		_mark = mark;
	}

	protected abstract InputStream getInputStream() throws IOException;

	public <T> T read(InputStreamConsumer<T> consumer) throws IOException
	{
		InputStream is = null;
		T t = null;
		try
		{
			is = getInputStream();
			t = consumer.consume(is);
		}
		finally
		{
			if (is != null)
			{
				is.close();
			}
		}

		return t;
	}

	public String readString() throws IOException
	{
		return read(new InputStreamConsumer<String>()
		{

			public String consume(InputStream is) throws IOException
			{
				return IOUtils.toString(is);
			}
		});
	}

	public byte[] readBytes() throws IOException
	{
		return read(new InputStreamConsumer<byte[]>()
		{

			public byte[] consume(InputStream is) throws IOException
			{
				return IOUtils.toByteArray(is);
			}
		});
	}

	public void syncLength() throws IOException
	{
		setLength(read(new InputStreamConsumer<Long>()
		{
			public Long consume(InputStream is) throws IOException
			{
				long count = 0;
				while (true)
				{
					byte[] bytes = new byte[1024];
					int read = is.read(bytes);
					if (read <= 0)
					{
						break;
					}
					count += read;
				}

				return count;
			}
		}));
	}
}