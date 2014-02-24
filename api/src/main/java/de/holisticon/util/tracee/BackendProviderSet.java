package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.lang.ref.SoftReference;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This set is an immutable value holder for the cache of TraceeBackendProvider.
 * There are some reasons why we need this abstraction:<br />
 * <p/>
 * * If one entry get collected by the garbage collector the whole collection is empty. This forces the resolver to search
 * for resolvers again.
 * * Hold the SoftReferences in a Collection avoid code confusing and error prone code in the resolver itself
 * * It's easier to test the class.
 */
class BackendProviderSet extends AbstractSet<TraceeBackendProvider> {

	private Set<SoftReference<TraceeBackendProvider>> values;

	// Never ever set this boolean to true if it was false! 
	private boolean valid = true;

	BackendProviderSet(Set<TraceeBackendProvider> elements) {
		this.values = new HashSet<SoftReference<TraceeBackendProvider>>();
		addAllInternal(elements);
	}

	private void addAllInternal(final Collection<TraceeBackendProvider> elements) {
		for (TraceeBackendProvider element : elements) {
			values.add(new SoftReference<TraceeBackendProvider>(element));
		}
	}

	@Override
	public Iterator<TraceeBackendProvider> iterator() {
		Collection<TraceeBackendProvider> strongRefList = createStrongView(values);
		determineValidity(strongRefList);
		if (valid) {
			return strongRefList.iterator();
		}
		return Collections.<TraceeBackendProvider>emptyList().iterator();
	}

	@Override
	public int size() {
		Collection<TraceeBackendProvider> strongRefList = createStrongView(values);
		determineValidity(strongRefList);
		if (valid) {
			return strongRefList.size();
		}
		return 0;
	}

	private void determineValidity(final Collection<TraceeBackendProvider> values) {
		if (!valid) {
			return;
		}
		for (TraceeBackendProvider value : values) {
			// Missed value detected. This Set is not valid!
			if (value == null)
				valid = false;
		}
	}

	private Collection<TraceeBackendProvider> createStrongView(Collection<SoftReference<TraceeBackendProvider>> values) {
		List<TraceeBackendProvider> strongRefs = new ArrayList<TraceeBackendProvider>(values.size());
		for (SoftReference<TraceeBackendProvider> value : values) {
			strongRefs.add(value.get());
		}
		return strongRefs;
	}
}