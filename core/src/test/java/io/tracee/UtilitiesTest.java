package io.tracee;

import org.junit.Test;

import java.util.regex.Pattern;

import static io.tracee.TraceeConstants.CONVERSATION_ID_KEY;
import static io.tracee.TraceeConstants.REQUEST_ID_KEY;
import static io.tracee.TraceeConstants.SESSION_ID_KEY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

public class UtilitiesTest {

    @Test
    public void testCreateRandomAlphanumeric() {
        final String randomAlphanumeric = Utilities.createRandomAlphanumeric(32);
		assertTrue("32 alphanumeric character sequence",
				Pattern.compile("[0-9a-zA-Z]{32}").matcher(randomAlphanumeric).matches());
	}

	@Test
	public void testIsNullOrEmptyString() {
		assertThat(Utilities.isNullOrEmptyString(null), is(true));
		assertThat(Utilities.isNullOrEmptyString(""), is(true));
		assertThat(Utilities.isNullOrEmptyString(" "), is(true));
		assertThat(Utilities.isNullOrEmptyString(" \n\t"), is(true));
		assertThat(Utilities.isNullOrEmptyString("a"), is(false));
	}

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

	@Test
	public void ignoreRequestIdGenerationIfBackendIsNull() {
		Utilities.generateRequestIdIfNecessary(null);
		assertTrue("No exception occurred", true);
	}

	@Test
	public void dontGenerateRequestIdIfPresentInBackend() {
		final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
		backend.put(REQUEST_ID_KEY, "ourTestId");
		Utilities.generateRequestIdIfNecessary(backend);
		assertThat(backend.get(REQUEST_ID_KEY), is("ourTestId"));
	}

	@Test
	public void generateRequestIdIfNotPresentInBackend() {
		final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
		Utilities.generateRequestIdIfNecessary(backend);
		assertThat(backend.get(REQUEST_ID_KEY), not(isEmptyOrNullString()));
	}

	@Test
	public void ignoreSessionIdGenerationIfBackendIsNull() {
		Utilities.generateSessionIdIfNecessary(null, "abc");
		assertTrue("No exception occurred", true);
	}

	@Test
	public void dontGenerateSessionIdIfPresentInBackend() {
		final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
		backend.put(SESSION_ID_KEY, "ourTestId");
		Utilities.generateSessionIdIfNecessary(backend, "123");
		assertThat(backend.get(SESSION_ID_KEY), is("ourTestId"));
	}

	@Test
	public void generateSessionIdIfNotPresentInBackend() {
		final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
		Utilities.generateSessionIdIfNecessary(backend, "123");
		assertThat(backend.get(SESSION_ID_KEY), equalTo(Utilities.createAlphanumericHash("123",
				PermitAllTraceeFilterConfiguration.ARBITRARY_NUMBER)));
	}

	@Test
	public void ignoreConversationIdGenerationIfBackendIsNull() {
		Utilities.generateConversationIdIfNecessary(null);
		assertTrue("No exception occurred", true);
	}

	@Test
	public void dontGenerateConversationIdIfPresentInBackend() {
		final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
		backend.put(CONVERSATION_ID_KEY, "ourTestId");
		Utilities.generateConversationIdIfNecessary(backend);
		assertThat(backend.get(CONVERSATION_ID_KEY), is("ourTestId"));
	}

	@Test
	public void generateConversationIdIfNotPresentInBackend() {
		final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
		final String conversationId = Utilities.generateConversationIdIfNecessary(backend);
		assertThat(backend.get(CONVERSATION_ID_KEY), equalTo(conversationId));
	}
}
