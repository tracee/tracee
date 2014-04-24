package de.holisticon.util.tracee;

import java.util.Enumeration;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public final class EmptyEnumeration<T> implements Enumeration<T> {

	public static <T> EmptyEnumeration<T> emptyEnumeration() {
		return new EmptyEnumeration<T>();
	}

	@Override
	public boolean hasMoreElements() {
		return false;
	}

	@Override
	public T nextElement() {
		return null;
	}
}
