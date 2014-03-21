package jenablob.store;

import java.io.IOException;

public class BadBlobInputStreamException extends IOException
{
	private String _handle;

	public String getHandle()
	{
		return _handle;
	}

	public void setHandle(String handle)
	{
		_handle = handle;
	}

	public BadBlobInputStreamException(String handle)
	{
		_handle = handle;
	}
}
