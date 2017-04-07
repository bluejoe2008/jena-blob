package jenablob;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author bluejoe2008@gmail.com
 */
public interface InputStreamConsumer<T>
{
	public T consume(InputStream is) throws IOException;
}