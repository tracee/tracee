package io.tracee.testhelper;

import java.lang.reflect.Field;

public final class FieldAccessUtil {

	private FieldAccessUtil() {
	}

	public static <T> T getFieldVal(final Object obj, String fieldName) {
		final Class<?> objClass = obj.getClass();
		return getFieldVal(obj, objClass, fieldName);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getFieldVal(Object obj, Class<?> objClass, String fieldName) {
		Field field = null;
		try {
			field = objClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) (field.get(obj));
		} catch (NoSuchFieldException e) {
			if (objClass.getSuperclass() != null)
				return (T) getFieldVal(obj, objClass.getSuperclass(), fieldName);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} finally {
			if (field != null) {
				field.setAccessible(false);
			}
		}
	}
}
