package de.holisticon.util.tracee;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class UtilitiesTest {

    @Test
    public void shouldCreateStringWithSpecificLength() {
        final String randomAlphanumeric = Utilities.createRandomAlphanumeric(32);
        assertThat("length", randomAlphanumeric.length(), equalTo(32));
    }
	
	@Test
	public void shouldCreateRandomString() {
		final String randA = Utilities.createRandomAlphanumeric(10);
		final String randB = Utilities.createRandomAlphanumeric(10);
		assertThat(randA, not(containsString(randB)));
	}
	
	@Test
	public void shouldCreateSameHashForSameString() {
		final String hashA = Utilities.createAlphanumericHash("ABC", 20);
		final String hashB = Utilities.createAlphanumericHash("ABC", 20);
		assertThat(hashA, equalTo(hashB));
	}	
	
	@Test
	public void shouldCreateDifferentHashForDifferentString() {
		final String hashA = Utilities.createAlphanumericHash("ABCE", 20);
		final String hashB = Utilities.createAlphanumericHash("ABCD", 20);
		assertThat(hashA, not(equalTo(hashB)));
	}
	
	@Test
	public void shouldCreateHashInCorrectLength() {
		final String hash = Utilities.createAlphanumericHash("ABCE", 25);
		assertThat(hash.length(), comparesEqualTo(25));
	}
	
	@Test
	public void shouldRepeatHashesIfLengthIsToLong() {
		final String hash = Utilities.createAlphanumericHash("ABCD", 500);
		assertThat(hash.length(), comparesEqualTo(500));
	}
	
	@Test
	public void shouldHandleNormalSha256Length() {
		final String hash = Utilities.createAlphanumericHash("ABCD", 77);
		assertThat(hash.length(), comparesEqualTo(77));
	}
}
