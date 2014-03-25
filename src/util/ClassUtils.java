package util;

import java.lang.reflect.Field;

import org.apache.commons.lang.reflect.FieldUtils;
/**
 * @author bluejoe2008@gmail.com
 */
public class ClassUtils
{
	private static <T> T _getFieldValue(Object o, String fieldName) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException
	{
		Field field = FieldUtils.getField(o.getClass(), fieldName, true);
		return (T) field.get(o);
	}

	private static void _setFieldValue(Object o, String fieldName, Object value) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchFieldException
	{
		Field field = FieldUtils.getField(o.getClass(), fieldName, true);
		field.set(o, value);
	}

	public static <T> T getFieldValue(Object o, String fieldNamePath) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException
	{
		String[] fieldNames = fieldNamePath.split("\\.");
		for (String fn : fieldNames)
		{
			o = _getFieldValue(o, fn);
		}

		return (T) o;
	}

	public static void setFieldValue(Object o, String fieldNamePath, Object value) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchFieldException
	{
		String[] fieldNames = fieldNamePath.split("\\.");
		for (int i = 0; i < fieldNames.length - 1; i++)
		{
			o = _getFieldValue(o, fieldNames[i]);
		}

		_setFieldValue(o, fieldNames[fieldNames.length - 1], value);
	}
}
