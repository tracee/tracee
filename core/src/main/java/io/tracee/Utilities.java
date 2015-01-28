package io.tracee;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


public final class Utilities {

	private static final char[] ALPHANUMERICS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	private Utilities() {
		// hide constructor
	}

	public static boolean isNullOrEmptyString(final String value) {
		return value == null || value.trim().isEmpty();
	}

	/**
	 * Creates a random Strings consisting of alphanumeric charaters with a length of 32.
	 */
	public static String createRandomAlphanumeric(final int length) {
		final Random r = ThreadLocalRandom.current();
		final char[] randomChars = new char[length];
		for (int i = 0; i < length; ++i) {
			randomChars[i] = ALPHANUMERICS[r.nextInt(ALPHANUMERICS.length)];
		}
		return new String(randomChars);
	}

	/**
	 * Creates a alphanumeric projection with a given length of the given object using its {@link Object#hashCode()}.
	 */
	public static String createAlphanumericHash(final String str, final int length) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-256");
			final byte[] digest = md.digest(str.getBytes(Charset.forName("UTF-8")));
			// To human
			final StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				if (b < 16) sb.append("0");
				sb.append(Integer.toHexString(b & 0xff));
			}
			// repeat if to small
			while (sb.length() < length) {
				sb.append(sb.toString());
			}
			// truncation and return
			return sb.delete(length, sb.length()).toString();
		} catch (NoSuchAlgorithmException e) {
			// Preferred hash algorithm is not available. We generate random string.
			return createRandomAlphanumeric(length);
		} catch (UnsupportedCharsetException e) {
			// We should handle such error like the NoSuchAlgorithmException
			return createRandomAlphanumeric(length);
		}
	}

	/**
	 * Generate request id if it doesn't exist in TraceeBackend and configuration asks for one
	 *
	 * @param backend Currently used TraceeBackend
	 */
	public static void generateRequestIdIfNecessary(final TraceeBackend backend) {
		if (backend != null && !backend.containsKey(TraceeConstants.REQUEST_ID_KEY) && backend.getConfiguration().shouldGenerateRequestId()) {
			backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(backend.getConfiguration().generatedRequestIdLength()));
		}
	}

	/**
	 * Generate session id hash if it doesn't exist in TraceeBackend and configuration asks for one
	 *
	 * @param backend Currently used TraceeBackend
	 * @param sessionId Current http sessionId
	 */
	public static void generateSessionIdIfNecessary(final TraceeBackend backend, final String sessionId) {
		if (backend != null && !backend.containsKey(TraceeConstants.SESSION_ID_KEY) && backend.getConfiguration().shouldGenerateSessionId()) {
			backend.put(TraceeConstants.SESSION_ID_KEY, Utilities.createAlphanumericHash(sessionId, backend.getConfiguration().generatedSessionIdLength()));
		}
	}

	/**
	 * Generate conversation id hash if it doesn't exist in TraceeBackend and configuration asks for one
	 *
	 * @param backend Currently used TraceeBackend
	 */
	public static String generateConversationIdIfNecessary(final TraceeBackend backend) {
		if (backend != null && !backend.containsKey(TraceeConstants.CONVERSATION_ID_KEY) && backend.getConfiguration().shouldGenerateConversationId()) {
			backend.put(TraceeConstants.CONVERSATION_ID_KEY, Utilities.createRandomAlphanumeric(backend.getConfiguration().generatedConversationIdLength()));
		}
		return backend != null ? backend.get(TraceeConstants.CONVERSATION_ID_KEY) : null;
	}
}
