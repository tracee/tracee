package io.tracee;

import io.tracee.spi.TraceeBackendProvider;

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
		this.values = new HashSet<>();
		addAllInternal(elements);
	}

	private void addAllInternal(final Collection<TraceeBackendProvider> elements) {
		for (TraceeBackendProvider element : elements) {
			values.add(new SoftReference<>(element));
		}
	}

	@Override
	public Iterator<TraceeBackendProvider> iterator() {
		final Collection<TraceeBackendProvider> strongRefList = createStrongView(values);
		determineValidity(strongRefList);
		if (valid) {
			return strongRefList.iterator();
		}
		return Collections.<TraceeBackendProvider>emptyList().iterator();
	}

	@Override
	public int size() {
		final Collection<TraceeBackendProvider> strongRefList = createStrongView(values);
		determineValidity(strongRefList);
		if (valid) {
			return strongRefList.size();
		}
		return 0;
	}

	private void determineValidity(final Collection<TraceeBackendProvider> providers) {
		if (!valid) {
			return;
		}
		for (TraceeBackendProvider provider : providers) {
			// Missed value detected. This Set is not valid!
			if (provider == null)
				valid = false;
		}
	}

	private Collection<TraceeBackendProvider> createStrongView(Collection<SoftReference<TraceeBackendProvider>> providerReferences) {
		final List<TraceeBackendProvider> strongRefs = new ArrayList<>(providerReferences.size());
		for (SoftReference<TraceeBackendProvider> providerSoftReference : providerReferences) {
			strongRefs.add(providerSoftReference.get());
		}
		return strongRefs;
	}
}
