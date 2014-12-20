package io.tracee;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

public class ThreadLocalRandomTest {

	private static final ThreadLocalRandom random = new ThreadLocalRandom();

	@Test(expected = IllegalArgumentException.class)
	public void nextLong_should_throw_illegalArgumentException_if_n_is_zero() {
		random.nextLong(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nextLong_should_throw_illegalArgumentException_if_n_is_subzero() {
		random.nextLong(-1);
	}

	@Test
	public void nextLong_is_one_if_n_is_one() {
		assertThat(random.nextLong(1), equalTo(0L));
	}

	@Test
	public void nextLong_is_less_than_n() {
		assertThat(random.nextLong(42), lessThan(42L));
	}

	@Test
	public void nextLong_is_less_than_integer_max_if_n_is_greater_than_integer_max() {
		assertThat(random.nextLong(Integer.MAX_VALUE + 42L), lessThan((long) Integer.MAX_VALUE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void nextLong_throws_illegalArgumentException_if_upper_bound_less_than_lower_bound() {
		random.nextLong(Integer.MAX_VALUE + 42L, Integer.MAX_VALUE + 23L);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nextLong_throws_illegalArgumentException_if_upper_bound_equals_lower_bound() {
		random.nextLong(Integer.MAX_VALUE + 42L, Integer.MAX_VALUE + 42L);
	}

	@Test
	public void nextLong_equals_lower_bound_if_upper_bound_equals_lower_bound_plus_one() {
		assertThat(random.nextLong(Integer.MAX_VALUE + 42L, Integer.MAX_VALUE + 43L), equalTo(Integer.MAX_VALUE + 42L));
	}

	@Test(expected = IllegalArgumentException.class)
	public void nextInt_throws_illegalArgumentException_if_upper_bound_less_than_lower_bound() {
		random.nextInt(42, 23);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nextInt_throws_illegalArgumentException_if_upper_bound_equals_lower_bound() {
		random.nextInt(42, 42);
	}

	@Test
	public void nextInt_equals_lower_bound_if_upper_bound_equals_lower_bound_plus_one() {
		assertThat(random.nextInt(42, 43), equalTo(42));
	}

	/*
	 * Test against http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7051516
	 */
	@Test
	public void differentThreadsShouldReturnDifferentRequestIds() throws InterruptedException {
		final Map<String, List<Integer>> threadValues = new ConcurrentHashMap<String, List<Integer>>();

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final List<Integer> values = new ArrayList<Integer>(20);
				threadValues.put(Thread.currentThread().getName(), values);
				for (int i = 0; i < 20; i++)
					values.add(ThreadLocalRandom.current().nextInt());
			}
		};

		createThreadsAndWaitForFinish(runnable);

		final List<Integer> valuesThread1 = threadValues.get("thread1");
		final List<Integer> valuesThread2 = threadValues.get("thread2");

		assertThat(valuesThread1, not(hasItems(valuesThread2.toArray(new Integer[valuesThread2.size()]))));
	}

	private void createThreadsAndWaitForFinish(Runnable runnable) throws InterruptedException {
		final Thread thread1 = new Thread(runnable, "thread1");
		final Thread thread2 = new Thread(runnable, "thread2");
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();
	}
}
