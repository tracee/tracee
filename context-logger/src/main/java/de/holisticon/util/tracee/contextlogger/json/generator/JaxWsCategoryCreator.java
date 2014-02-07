package de.holisticon.util.tracee.contextlogger.json.generator;

import de.holisticon.util.tracee.contextlogger.json.beans.JaxWsCategory;

/**
 * Factory for jaxws context specific data.
 * Created by TGI on 19.12.13.
 */
public final class JaxWsCategoryCreator {

    private JaxWsCategoryCreator() {

    }

    public static JaxWsCategory createJaxWsCategory(String request, String response) {

        return new JaxWsCategory(request, response);

    }
}
