package util;

import java.lang.reflect.Field;

public class ClassUtils
{

	public static <T> T getFieldValue(Object o, Class<T> clazz) throws IllegalArgumentException, IllegalAccessException
	{
		for (Field field : o.getClass().getDeclaredFields())
		{
			if (clazz.isAssignableFrom(field.getType()))
			{
				field.setAccessible(true);
				return (T) field.get(o);
			}
		}

		return null;
	}
}
