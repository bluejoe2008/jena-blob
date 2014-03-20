package jenablob.lit;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamConsumer<T>
{
	public T consume(InputStream is) throws IOException;
}