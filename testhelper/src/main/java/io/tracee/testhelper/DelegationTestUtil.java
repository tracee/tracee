package io.tracee.testhelper;

import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;

public final class DelegationTestUtil {

	private static final Set<String> BLACKLIST_METHOD = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			"class$", "finalize", "equals", "hashCode", "toString", "clone", "newInstance"
	)));

	public static <I, W> Delegation<I, W> assertDelegationToSpy(I innerObj) {
		return new Delegation<>(innerObj);
	}

	public static class Delegation<I, W> {
		private final I innerObj;
		private W wrapperObj;
		private final Set<String> ignoreMethods = new HashSet<>();

		Delegation(I innerObj) {
			this.innerObj = innerObj;
		}

		public Delegation<I, W> by(final W wrapperObj) {
			this.wrapperObj = wrapperObj;
			return this;
		}

		public Delegation<I, W> ignore(String methodName) {
			ignoreMethods.add(methodName);
			return this;
		}

		public void verify() {
			if(!Mockito.mockingDetails(innerObj).isMock()) {
				throw new IllegalStateException("Inner object is no Mockito mock!");
			}
			if(Mockito.mockingDetails(wrapperObj).isMock()) {
				throw new IllegalStateException("Wrapper objecgt should be real class with mocked inner object inside");
			}

			String errorMsg = "";
			try {
				final Method[] wrapperMethods = wrapperObj.getClass().getDeclaredMethods();
				final Map<String, Method> innerMethods = new HashMap<>();
				for (Method innerMethod : innerObj.getClass().getDeclaredMethods()) {
					if (Modifier.isPublic(innerMethod.getModifiers())) {
						innerMethods.put(innerMethod.getName() + " :: " + paramsToStr(innerMethod.getParameterTypes()), innerMethod);
					}
				}

				for (Method wrapperMethod : wrapperMethods) {
					if (innerMethods.containsKey(wrapperMethod.getName() + " :: " + paramsToStr(wrapperMethod.getParameterTypes()))
							&& !BLACKLIST_METHOD.contains(wrapperMethod.getName())
							&& !ignoreMethods.contains(wrapperMethod.getName())) {
						errorMsg = "Method not delegated: " + wrapperMethod.getName();

						final Object[] arguments = generateMockedParams(wrapperMethod);

						wrapperMethod.invoke(wrapperObj, arguments);
						innerMethods.get(wrapperMethod.getName() + " :: " + paramsToStr(wrapperMethod.getParameterTypes())).invoke(Mockito.verify(innerObj), arguments);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(errorMsg + "\n" + e.getMessage(), e);
			}
		}
	}

	private static Object[] generateMockedParams(Method wrapperMethod) {
		final Class<?>[] parameterTypes = wrapperMethod.getParameterTypes();
		final List<Object> arguments = new ArrayList<>();
		for (Class<?> parameterType : parameterTypes) {
			if ("boolean".equals(parameterType.getName())) {
				arguments.add(Boolean.FALSE);
			} else if ("int".equals(parameterType.getName())) {
				arguments.add(0);
			} else if ("long".equals(parameterType.getName())) {
				arguments.add(0L);
			} else if (parameterType == String.class) {
				arguments.add("");
			} else {
				arguments.add(mock(parameterType));
			}
		}
		return arguments.toArray(new Object[arguments.size()]);
	}

	private static String paramsToStr(Class<?>[] parameterTypes) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0, parameterTypesLength = parameterTypes.length; i < parameterTypesLength; i++) {
			Class<?> type = parameterTypes[i];
			sb.append(type.getName());
			if (i < (parameterTypesLength - 1)) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	private DelegationTestUtil() {
	}
}
