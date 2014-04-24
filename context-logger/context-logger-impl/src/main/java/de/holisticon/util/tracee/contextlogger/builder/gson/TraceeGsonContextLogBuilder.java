package de.holisticon.util.tracee.contextlogger.builder.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.holisticon.util.tracee.contextlogger.builder.AbstractContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedDataContextProvider;
import de.holisticon.util.tracee.contextlogger.profile.ProfileSettings;

/**
 * Context Logger implementation for gson.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public class TraceeGsonContextLogBuilder extends AbstractContextLogBuilder {


	private Gson gson = null;

	/**
	 * Gets or creates the gson instance generation of output.
	 */
	private Gson getOrCreateGson() {

		if (gson == null) {

			final GsonBuilder gsonBuilder = new GsonBuilder();

			final TraceeGenericGsonSerializer gsonSerializer = new TraceeGenericGsonSerializer(new ProfileSettings(this.getProfile(),
					this.getManualContextOverrides()));

			for (Class clazz : this.getWrapperClasses()) {
				gsonBuilder.registerTypeAdapter(clazz, gsonSerializer);
			}

			gson = gsonBuilder.create();
		}

		return gson;

	}


	@Override
	public final String log(Object... instancesToLog) {
		return getOrCreateGson().toJson(instancesToLog);
	}

	@Override
	public final String logPassedContext(PassedDataContextProvider passedContextData) {
		return getOrCreateGson().toJson(passedContextData);
	}


}
