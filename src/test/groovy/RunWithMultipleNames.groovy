import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.METHOD
import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

@Retention(RUNTIME)
@Target([METHOD, TYPE])
@ExtensionAnnotation(RunWithMultipleNamesExtension.class)
@interface RunWithMultipleNames {
}
