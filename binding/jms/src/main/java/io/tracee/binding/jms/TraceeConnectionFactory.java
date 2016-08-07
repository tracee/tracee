package io.tracee.binding.jms;


import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class TraceeConnectionFactory implements ConnectionFactory {

	private final ConnectionFactory delegate;
	private final TraceeBackend traceeBackend;
	private final TraceeFilterConfiguration filterConfiguration;

	public TraceeConnectionFactory(ConnectionFactory delegate, TraceeBackend traceeBackend, TraceeFilterConfiguration filterConfiguration) {
		this.delegate = delegate;
		this.traceeBackend = traceeBackend;
		this.filterConfiguration = filterConfiguration;
	}

	@Override
	public Connection createConnection() throws JMSException {
		//TODO: proxy to TraceeConnection
		return delegate.createConnection();
	}

	@Override
	public Connection createConnection(String userName, String password) throws JMSException {
		//TODO: proxy to TraceeConnection
		return delegate.createConnection();
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeConnectionFactory) {
			return delegate.equals(((TraceeConnectionFactory)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
