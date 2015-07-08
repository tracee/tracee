package io.tracee.testhelper;

import java.util.Random;

public final class PortUtil {

	public static int randomTestPort() {
		int port;
		//noinspection StatementWithEmptyBody
		while ((port = new Random().nextInt(65536)) < 10000) {
		}
		return port;
	}

	private PortUtil() {
	}
}
