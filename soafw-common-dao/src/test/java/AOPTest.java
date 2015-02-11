import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kjt.common.cache.dao.ICacheVersionDAO;
import com.kjt.common.cache.dao.model.CacheVersion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:springtest.xml")
public class AOPTest {
  @Resource(name = "CacheVersion")
  private ICacheVersionDAO<CacheVersion> cacheVersionDAO;

  @Test
  public void aa() {
    cacheVersionDAO.incrObjVersion("authorization", null);
  }
}
