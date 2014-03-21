package jenablob.store;

import java.io.IOException;

/**
 * @author bluejoe2008@gmail.com
 */
public class BadBlobInputStreamException extends IOException
{
	private String _handle;

	public BadBlobInputStreamException(String handle)
	{
		_handle = handle;
	}

	public String getHandle()
	{
		return _handle;
	}

	public void setHandle(String handle)
	{
		_handle = handle;
	}
}
