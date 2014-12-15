> This document contains documentation for the tracee-core module. Click [here](/README.md) to get an overview that TracEE is about.

This module contains implementation details and may be used by implementing features.

For a user manual please visit the [tracee-api](/api) documentation.

# Filter Configuration

If not configured otherwise, all TracEE connector modules gracefully accept incoming contexts,
pass them down to sub-invocations and post the resulting contexts back to the caller.
This behaviour is not always desirable, especially if the context may contain sensitive information.

This is where TracEE configuration kicks in. It allows you to configure each connector with its own accepting-
and passing-policy. These policies are called __profiles__. TracEE ships with a small set of standard profiles. They
are encoded within java property files.

The preconfigured profiles are shipped with the `tracee-core` module under [META-INF/tracee.default.properties](META-INF/tracee.default.properties).

You may create new profiles within your application or overwrite values from the tracee.default.properties file.
Just create a file with the name `META-INF/tracee.properties` in your application resource directory.

## Configurable Values

| Configuration key          | Description |
|:---------------------------|:------------|
| .IncomingRequest           | Comma-separated list of regular expressions. Each parameter name matched by at least one of the expressions will be parsed from incoming requests to the context. |
| .OutgoingResponse          | Comma-separated list of regular expressions. Each parameter name matched by at least one of the expressions will be written back from the context to the outgoing response. |
| .OutgoingRequests          | Comma-separated list of regular expressions. Each parameter name matched by at least one of the expressions will be written from the current context to each outgoing request. |
| .OutgoingResponse          | Comma-separated list of regular expressions. Each parameter name matched by at least one of the expressions will be parsed from each incoming response to the current context. |
| .AsyncDispatch             | Comma-separated list of regular expressions. Each parameter name matched by at least one of the expressions will be passed along with asynchronous dispatched messages. |
| .AsyncProcess              | Comma-separated list of regular expressions. Each parameter name matched by at least one of the expressions will be taken up while processing asynchronous messages. |
| .requestIdLength           | The length of the generated request ids. To disable id request generation, set this value to `0`. |
| .sessionIdLength           | The length of the generated session ids. To disable id generation, set this value to `0`. |


## Configuration resolution

Loading order:
* TracEE first scans for __all__ `META-INF/tracee.default.properties` files _in undefined order_ on the classpath and combines them to a _default_-properties object.
* Then TracEE scans for __all__ `META-INF/tracee.properties` files _in undefined order_ on the classpath and combines them to an _application_-properties object.

Evaluation strategy:
* When a connector is configured with a profile, it will lookup the configuration keys with the prefix `tracee.profile.PROFILE_NAME.`.
* When there is no profile specific configuration for a certain key, it will fall back to the default configuration of the key `tracee.default.`.

If no profile is configured for a connector, it will always lookup the default value.

_Note: The behaviour cannot yet be configured in a client-dependent way_
