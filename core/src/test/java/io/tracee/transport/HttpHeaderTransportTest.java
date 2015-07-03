package io.tracee.transport;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HttpHeaderTransportTest {

	final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	final HttpHeaderTransport UNIT = new HttpHeaderTransport();

	@Test
	public void renderKeyValueWithEqualSignAndConcatWithComma() {
		final Map<String, String> context = new HashMap<String, String>();
		context.put("key1", "value1");
		context.put("key2", "value2");
		assertThat(UNIT.render(context), anyOf(is("key1=value1,key2=value2"), is("key2=value2,key1=value1")));
	}

	@Test
	public void trimValuesBeforeConcat() {
		final Map<String, String> context = new HashMap<String, String>();
		context.put(" key1 ", " value1 ");
		context.put(" key2 ", " value2 ");
		System.out.println(UNIT.render(context));
		assertThat(UNIT.render(context), anyOf(is("key1=value1,key2=value2"), is("key2=value2,key1=value1")));
	}

	@Test
	public void renderEmptyMapResultsIntoEmptyString() {
		assertThat(UNIT.render(Collections.<String, String>emptyMap()), is(""));
	}

	@Test
	public void specialCharsShouldGetEscapedToNonUnsafeCharacters() {
		final Map<String, String> context = new HashMap<String, String>();
		context.put("!\"§$%&@/( )=?", "^°+*'#-.,;:_<> ´´``");
		context.put("ö üäß", "Ö ÜÄß€");
		final String headerString = UNIT.render(context);
		assertThat(headerString, not(anyOf(containsString("\""), containsString("\\"))));
	}

	@Test
	public void specialCharsShouldGetEscapedToNotUsedSeperatorCharacters() {
		final Map<String, String> context = new HashMap<String, String>();
		context.put("!\"§$%&@/()=? ", "^°+*'# .,;:_<>´´``");
		context.put("öü äß", "ÖÜ Äß€");
		final String headerString = UNIT.render(context);
		assertThat(headerString, not(anyOf(containsString("("), containsString(")"))));
		assertThat(headerString, not(anyOf(containsString("<"), containsString(">"))));
		assertThat(headerString, not(anyOf(containsString("{"), containsString("}"))));
		assertThat(headerString, not(anyOf(containsString("["), containsString("]"))));
		assertThat(headerString, not(anyOf(containsString("/"), containsString("\\"))));
		assertThat(headerString, not(anyOf(containsString(" "), containsString("\t"))));
		assertThat(headerString, not(anyOf(containsString("?"), containsString(":"))));
		assertThat(headerString, not(anyOf(containsString(";"), containsString("@"))));
	}

	@Test
	public void specialCharsShouldGetEscapedAndUseOnlyCommaAndEqualsSignForConcat() {
		final Map<String, String> context = new HashMap<String, String>();
		context.put("!\"§$%&@/()=? ", "^°+*'# .,;:_<>´´``");
		context.put("öü äß", "ÖÜ Äß€");
		final String headerString = UNIT.render(context);
		assertThat(countChars(headerString, '='), is(2)); // two pairs
		assertThat(countChars(headerString, ','), is(1)); // one concatenation
	}

	@Test
	public void emptyHeaderShouldReturnEmptyMap() {
		final Map<String, String> contextMap = UNIT.parse("");
		assertThat(contextMap, is(notNullValue()));
		assertThat(contextMap.isEmpty(), is(true));
	}

	@Test
	public void parseHeaderWithoutAnySpecialChars() {
		final String header = "key1=value1,key2=value2";
		final Map<String, String> context = UNIT.parse(header);
		assertThat(context, hasEntry("key1", "value1"));
		assertThat(context, hasEntry("key2", "value2"));
	}

	@Test
	public void specialCharactersShouldBeParsedBackIntoContextMap() {
		final String header = "%C3%B6+%C3%BC%C3%A4%C3%9F=%C3%96+%C3%9C%C3%84%C3%9F%E2%82%AC,%21%22%C2%A7%24%25%26%40%2F%28+%29%3D%3F=%5E%C2%B0%2B*%27%23-.%2C%3B%3A_%3C%3E+%C2%B4%C2%B4%60%60";
		final Map<String, String> context = UNIT.parse(header);
		assertThat(context, hasEntry("!\"§$%&@/( )=?", "^°+*'#-.,;:_<> ´´``"));
		assertThat(context, hasEntry("ö üäß", "Ö ÜÄß€"));
	}

	@Test
	public void trimHeaderBeforeParsing() {
		final String header = "  key1=value1,key2=value2  ";
		final Map<String, String> context = UNIT.parse(header);
		assertThat(context, hasEntry("key1", "value1"));
		assertThat(context, hasEntry("key2", "value2"));
	}

	@Test
	public void skipPairWithoutValuePart() {
		final String header = "key1=value1,key2";
		final Map<String, String> context = UNIT.parse(header);
		assertThat(context, hasEntry("key1", "value1"));
		assertThat(context.size(), is(1));
	}

	@Test
	public void skipPairWithTwoEqualSign() {
		final String header = "key1=value1=subvalue1,key2=value2";
		final Map<String, String> context = UNIT.parse(header);
		assertThat(context, hasEntry("key2", "value2"));
		assertThat(context.size(), is(1));
	}

	@Test
	public void headerWithCommaShouldReturnInEmptyMap() {
		final Map<String, String> contextMap = UNIT.parse(",");
		assertThat(contextMap, is(notNullValue()));
		assertThat(contextMap.isEmpty(), is(true));
	}

	@Test
	public void shouldParseTwoHeadersAndMergeThem() {
		final List<String> headers = Arrays.asList("key1=value1,key2=value2", "key3=value3");
		final Map<String, String> context = UNIT.parse(headers);
		assertThat(context, hasEntry("key1", "value1"));
		assertThat(context, hasEntry("key2", "value2"));
		assertThat(context, hasEntry("key3", "value3"));
		assertThat(context.size(), is(3));
	}

	private int countChars(String str, char c) {
		int charcount = 0;
		final char[] charArray = str.toCharArray();
		for (char c1 : charArray) {
			if (c1 == c)
				charcount++;
		}
		return charcount;
	}
}
