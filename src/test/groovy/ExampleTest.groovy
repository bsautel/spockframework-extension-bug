import spock.lang.Specification

@RunWithMultipleNames
class ExampleTest extends Specification {
    String name

    def setup(String name) {
        this.name = name
    }

    def 'should inject'() {
        expect:
        ["Alice", "Bob"].contains(name)
    }
}
