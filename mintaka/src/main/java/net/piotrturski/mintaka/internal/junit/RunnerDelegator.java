package net.piotrturski.mintaka.internal.junit;

import java.util.ArrayList;
import java.util.List;

import net.piotrturski.mintaka.TestWith;
import net.piotrturski.mintaka.internal.Executor;
import net.piotrturski.mintaka.internal.IoCContainer;
import net.piotrturski.mintaka.internal.SingleTestMethod;

import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class RunnerDelegator {

	IoCContainer ioCContainer = new IoCContainer();
	Executor executor = ioCContainer.getExecutor();
	
	protected List<FrameworkMethod> getParametrizedLeafMethods(TestClass testClass) {
		List<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
		List<FrameworkMethod> parametrizedMethods = testClass.getAnnotatedMethods(TestWith.class);
		for (FrameworkMethod parametrizedMethod : parametrizedMethods) {
			addInvocationsOfSingleParameterizedMethod(result, parametrizedMethod);
		}
		return result;
	}

	private void addInvocationsOfSingleParameterizedMethod(List<FrameworkMethod> result, FrameworkMethod parametrizedMethod) {
		TestWith testWithAnnotation = parametrizedMethod.getAnnotation(TestWith.class);
		for (int i = 0; i < testWithAnnotation.value().length; i++) {
			SingleTestMethod singleTestMethod = new SingleTestMethod(parametrizedMethod.getMethod(), i, executor);
			ParametrizedFrameworkMethod parametrizedFrameworkMethod = new ParametrizedFrameworkMethod(singleTestMethod);
			result.add(parametrizedFrameworkMethod);
		}
	}

	public List<FrameworkMethod> computeAllTestMethods(TestClass testClass2, List<FrameworkMethod> list) {
		ArrayList<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
		List<FrameworkMethod> annotatedMethods = getParametrizedLeafMethods(testClass2);
		result.addAll(annotatedMethods);
		result.addAll(list);
		return result;
	}

	public static boolean isParameterized(FrameworkMethod method) {
		return method.getAnnotation(TestWith.class) != null;
	}

	public static Description describeSingleInvocationOfParametrizedMethod(FrameworkMethod method, TestClass testClass) {
		ParametrizedFrameworkMethod parametrizedMethod = (ParametrizedFrameworkMethod) method;
		return RunnerDelegator.prepareDescription(testClass.getJavaClass(), method.getName(), parametrizedMethod.getParametersLine());
	}

	public static Description describeParametrizedMethodWithChildren(FrameworkMethod method, TestClass testClass) {
		// Description parent = Description.createTestDescription(testClass, method.getName());
		Description parent = RunnerDelegator.describeParentParametrizedMethod(method);
		String[] parameters = method.getAnnotation(TestWith.class).value();
		for (String parametersLine : parameters) {
			Description childDescription = RunnerDelegator.prepareDescription(testClass.getJavaClass(), method.getName(), parametersLine);
			parent.addChild(childDescription);
		}
		return parent;
	}

	public static List<FrameworkMethod> getClassChildrenToDescribe(TestClass testClass, List<FrameworkMethod> standardTestMethods) {
		List<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
		result.addAll(standardTestMethods);
		List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(TestWith.class);
		result.addAll(annotatedMethods);
		return result;
	}

	static Description describeParentParametrizedMethod(FrameworkMethod method) {
		// return Description.createSuiteDescription(method.getName()+ "("+testClass.getCanonicalName()+")");
		return Description.createSuiteDescription(method.getName());
	}

	static Description prepareDescription(Class<?> testClass, String methodName, String parametersLine) {
		//TODO test what if parameter contains '('
		return Description.createTestDescription(testClass, methodName + " [" + parametersLine + "]");
	}

}