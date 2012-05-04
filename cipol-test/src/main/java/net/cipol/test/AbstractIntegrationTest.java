package net.cipol.test;


import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(CleanInsertTestExecutionListener.class)
@Transactional
@ContextConfiguration({ "classpath*:META-INF/spring/*.xml" })
@ActiveProfiles(profiles = { "test" })
public abstract class AbstractIntegrationTest extends AbstractJUnit4SpringContextTests {

}
