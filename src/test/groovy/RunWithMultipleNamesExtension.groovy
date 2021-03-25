import org.spockframework.runtime.extension.IAnnotationDrivenExtension
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.MethodInfo
import org.spockframework.runtime.model.SpecInfo

import java.lang.reflect.Parameter;

class RunWithMultipleNamesExtension implements IAnnotationDrivenExtension<RunWithMultipleNames> {
    private String name

    @Override
    void visitSpecAnnotation(RunWithMultipleNames annotation, SpecInfo spec) {
        executeFeaturesWithAllGradleVersions(spec)
        makeNameAvailableInTheSetupMethod(spec)
    }

    protected void executeFeaturesWithAllGradleVersions(SpecInfo spec) {
        def bottomSpec = spec.getBottomSpec()
        def names = ["Alice", "Bob", "Charlie"]
        for (FeatureInfo feature : bottomSpec.getFeatures()) {
            feature.setReportIterations(true)
            feature.setIterationNameProvider({ "${name}" })
            feature.addInterceptor({ IMethodInvocation invocation ->
                for (String name : names) {
                    println("Running with ${name}")
                    this.name = name
                    invocation.proceed()
                }
            })
        }
    }

    protected void makeNameAvailableInTheSetupMethod(SpecInfo spec) {
        for (MethodInfo setupMethod : spec.getSetupMethods()) {
            setupMethod.addInterceptor({ IMethodInvocation invocation ->
                injectParameters(invocation, name)
                invocation.proceed()
            })
        }
    }

    private static void injectParameters(IMethodInvocation invocation, String name) {
        Map<Parameter, Integer> parameters = [:]
        invocation.method.reflection.parameters.eachWithIndex { parameter, i ->
            parameters << [(parameter): i]
        }
        parameters = parameters.findAll { it != MethodInfo.MISSING_ARGUMENT && String == it.key.type }
        parameters.each { parameter, i ->
            invocation.arguments[i] = name
        }
    }
}
