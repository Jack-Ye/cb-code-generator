package ${basePackageName}.${javaProjectName}.dao.impl;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-test.xml", "classpath:spring/applicationContext-dao.xml" })
@Transactional
@Commit
public class DefaultSampleDAOTest {
//    @Autowired
//    private SampleDAO sampleDAO;

//    @Test
//    public void test_sampleMethod() {
//      sampleDAO.sampleMethod();
//    }
}
