package de.holisticon.util.tracee;

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

	public static boolean isNullOrEmptyString(String value) {
		return value == null || value.trim().isEmpty();
	}

	/**
	 * Creates a random Strings consisting of alphanumeric charaters with a length of 32.
	 */
	public static String createRandomAlphanumeric(int length) {
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
	public static String createAlphanumericHash(String str, int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			final byte[] digest = md.digest(str.getBytes(Charset.forName("UTF-8")));
			// To human
			StringBuilder sb = new StringBuilder();
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
}
