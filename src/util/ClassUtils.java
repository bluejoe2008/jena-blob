package util;

import java.lang.reflect.Field;

public class ClassUtils
{
	private static <T> T _getFieldValue(Object o, String fieldName) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException
	{
		Field field = o.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T) field.get(o);
	}

	private static void _setFieldValue(Object o, String fieldName, Object value) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchFieldException
	{
		Field field = o.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(o, value);
	}

	public static <T> T getFieldValue(Object o, String fieldName) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException
	{
		String[] fieldNames = fieldName.split("\\.");
		for (String fn : fieldNames)
		{
			o = _getFieldValue(o, fn);
		}

		return (T) o;
	}

	public static void setFieldValue(Object o, String fieldName, Object value) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchFieldException
	{
		String[] fieldNames = fieldName.split("\\.");
		for (int i = 0; i < fieldNames.length - 1; i++)
		{
			o = _getFieldValue(o, fieldNames[i]);
		}

		_setFieldValue(o, fieldNames[fieldNames.length - 1], value);
	}
}
