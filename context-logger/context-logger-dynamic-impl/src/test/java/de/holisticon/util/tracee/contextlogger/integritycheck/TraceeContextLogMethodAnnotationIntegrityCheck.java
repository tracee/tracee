package de.holisticon.util.tracee.contextlogger.integritycheck;

import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair;
import de.holisticon.util.tracee.contextlogger.testdata.AnnotationTestClass;
import de.holisticon.util.tracee.contextlogger.testdata.TestClassWithMethods;
import de.holisticon.util.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tobias Gindler, holisticon AG on 18.03.14.
 */
public class TraceeContextLogMethodAnnotationIntegrityCheck {

    private Set<Class> ignoredTestClasses = new HashSet<Class>();
    {
        ignoredTestClasses.add(AnnotationTestClass.class);
        ignoredTestClasses.add(TestClassWithMethods.class);

    }

    @Test
    public void checkIntegrity () {

        Reflections reflections = new Reflections("de.holisticon.util.tracee.contextlogger");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TraceeContextLogProvider.class);

        for (Class clazz : annotated) {

            if (ignoredTestClasses.contains(clazz)) {
                continue;
            }

            for (Method method:clazz.getDeclaredMethods()) {

                boolean isTraceeContextLogMethodAnnotationSet = method.getAnnotation(TraceeContextLogProviderMethod.class) != null;
                boolean isFlattenAnnotationSet = method.getAnnotation(Flatten.class) != null;
                if (isTraceeContextLogMethodAnnotationSet) {

                    // check for public method
                    if (!TraceeContextLogAnnotationUtilities.checkIsPublic(method)) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " method is not public.");
                    }

                    if (!TraceeContextLogAnnotationUtilities.checkMethodHasNoParameters(method)) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " method has more than one parameter.");
                    }

                    if (!TraceeContextLogAnnotationUtilities.checkMethodHasNonVoidReturnType(method)) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " method has void as return type.");
                    }

                    if (isFlattenAnnotationSet && (!List.class.isAssignableFrom(method.getReturnType()) || !method.getGenericReturnType().toString().contains(NameValuePair.class.getCanonicalName()))) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " Method annotated with Flatten but has no return type of List<NameValuePair>.");
                    }

                } else if (isFlattenAnnotationSet){
                    Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " Flatten Annotation is set without TraceeContextLogProviderMethod Method.");
                }




            }

        }


    }




}
