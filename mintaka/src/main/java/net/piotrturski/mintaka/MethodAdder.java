package net.piotrturski.mintaka;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class MethodAdder {

	public List<FrameworkMethod> addParametrizedMethods(TestClass testClass) {
		List<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
		List<FrameworkMethod> parametrizedMethods = testClass.getAnnotatedMethods(TestWith.class);
		for (FrameworkMethod parametrizedMethod : parametrizedMethods) {
			TestWith testWithAnnotation = parametrizedMethod.getAnnotation(TestWith.class);
			for (int i = 0; i < testWithAnnotation.value().length; i++) {
				ParametrizedFrameworkMethod parametrizedFrameworkMethod = new ParametrizedFrameworkMethod(parametrizedMethod.getMethod(), i);
				result.add(parametrizedFrameworkMethod);
			}
		}
		return result;
	}
	
}
