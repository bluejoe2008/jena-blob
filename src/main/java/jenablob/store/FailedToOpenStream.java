package jenablob.store;

import java.io.IOException;

import jenablob.Blob;

/**
 * @author bluejoe2008@gmail.com
 */
public class FailedToOpenStream extends IOException
{
	private Blob _blob;

	public FailedToOpenStream(Blob blob)
	{
		_blob = blob;
	}

	public Blob getBlob()
	{
		return _blob;
	}
}
