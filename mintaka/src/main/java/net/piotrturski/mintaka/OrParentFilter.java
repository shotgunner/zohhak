package net.piotrturski.mintaka;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

class OrParentFilter extends Filter {
	private final Filter filter;

	static OrParentFilter decorate(Filter filter) {
		return new OrParentFilter(filter);
	}
	
	private OrParentFilter(Filter filter) {
		this.filter = filter;
	}

	@Override
	public boolean shouldRun(Description description) {
		if (filter.shouldRun(description)) {
			return true;
		}
		return filter.shouldRun(cutParamInfo(description));
	}

	@Override
	public String describe() {
		return filter.describe() + " with parameters or without";
	}
	
	private Description cutParamInfo(Description description) {
		String changed = description.getDisplayName().replaceAll(" \\[.*\\]", "");
		return Description.createSuiteDescription(changed);
	}
}