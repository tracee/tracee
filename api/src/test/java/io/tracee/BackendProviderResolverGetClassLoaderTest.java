package io.tracee;


import org.junit.After;
import org.junit.Test;

import java.security.Permission;

import static io.tracee.BackendProviderResolver.GetClassLoader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BackendProviderResolverGetClassLoaderTest {
	
	@Test
	public void shouldFetchContextClassloaderWithoutSecurityManager() {
		System.setSecurityManager(null);
		final ClassLoader cl = GetClassLoader.fromContext();
		assertThat(cl, is(this.getClass().getClassLoader()));
	}
	
	@Test
	public void shouldReturnClassloaderOfClassWithoutSecurityManager() {
		System.setSecurityManager(null);
		final ClassLoader cl = GetClassLoader.fromClass(this.getClass());
		assertThat(cl, is(this.getClass().getClassLoader()));
	}	
	
	@Test
	public void shouldFetchContextClassloaderWithSecurityManager() {
		System.setSecurityManager(new TestSecurityManager());
		final ClassLoader cl = GetClassLoader.fromContext();
		assertThat(cl, is(this.getClass().getClassLoader()));
	}
	
	@Test
	public void shouldReturnClassloaderOfClassWithSecurityManager() {
		System.setSecurityManager(new TestSecurityManager());
		final ClassLoader cl = GetClassLoader.fromClass(this.getClass());
		assertThat(cl, is(this.getClass().getClassLoader()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIAEifClassNull() {
		GetClassLoader.fromClass(null);
	}
	
	@After
	public void after() {
		System.setSecurityManager(null);
	}

	private class TestSecurityManager extends SecurityManager {
		@Override
		public void checkPermission(Permission perm) {
		}
	}
}
