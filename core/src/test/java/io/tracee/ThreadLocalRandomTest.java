package io.tracee;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

/**
 * @author Simon Sprünker (Holisticon AG)
 */
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
        assertThat(random.nextLong(Integer.MAX_VALUE + 42L), lessThan((long)Integer.MAX_VALUE));
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


}
