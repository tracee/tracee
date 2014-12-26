package io.tracee;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DelegationTestUtilTest {

	@Rule
	public ExpectedException exeptionRule = ExpectedException.none();

	@Test
	public void shouldFindAllDelegations() {
		final DelegationClass delegate = Mockito.spy(new DelegationClass());
		final RightWrapper rightWrapper = new RightWrapper(delegate);
		DelegationTestUtil.assertDelegationToSpy(delegate).by(rightWrapper).verify();
		assertThat(delegate.getInvotionCount(), is(3));
	}

	@Test
	public void shouldFindErrorsInDelegate() {
		exeptionRule.expect(RuntimeException.class);
		exeptionRule.expectMessage("Method not delegated: getInteger");

		final DelegationClass delegate = Mockito.spy(new DelegationClass());
		final WrongWrapper rightWrapper = new WrongWrapper(delegate);
		DelegationTestUtil.assertDelegationToSpy(delegate).by(rightWrapper).verify();
	}

	public class DelegationClass {

		private int invocationCount = 0;

		public int getInvotionCount() {
			return invocationCount;
		}

		public void setString(String str) {
			invocationCount++;
		}

		public void setInteger(int i) {
			invocationCount++;
		}

		public int getInteger() {
			invocationCount++;
			return 1;
		}
	}

	public class WrongWrapper extends DelegationClass {

		private final DelegationClass delegate;

		public WrongWrapper(DelegationClass delegate) {
			this.delegate = delegate;
		}

		@Override
		public void setString(String str) {
			delegate.setString(str);
		}

		@Override
		public int getInteger() {
			return -1;
		}

		@Override
		public void setInteger(int i) {
			delegate.setInteger(i);
		}
	}

	public class RightWrapper extends DelegationClass {

		private final DelegationClass delegate;

		public RightWrapper(DelegationClass delegate) {
			this.delegate = delegate;
		}

		@Override
		public void setString(String str) {
			delegate.setString(str);
		}

		@Override
		public int getInteger() {
			return delegate.getInteger();
		}

		@Override
		public void setInteger(int i) {
			delegate.setInteger(i);
		}
	}
}
