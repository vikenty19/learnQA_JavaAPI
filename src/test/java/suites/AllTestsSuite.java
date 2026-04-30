package suites;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SelectClasses;
import tests.KoelApiTest;
import tests.Schema_validator;


//@Suite
@SelectClasses({
        KoelApiTest.class,
        Schema_validator.class
})
public class AllTestsSuite {}