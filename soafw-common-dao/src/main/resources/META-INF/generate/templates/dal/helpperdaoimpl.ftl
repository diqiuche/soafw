package ${package}.dao.ibatis;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kjt.service.common.dao.ibatis.IBatisDAOException;

import ${package}.dao.I${name}HelpperDAO;
import ${package}.dao.ibatis.mapper.${name}HelpperMapper;
import ${package}.dao.model.${name};
import ${package}.dao.model.${name}Helpper;
import com.kjt.service.common.dao.ibatis.AbsHelpperIBatisDAOImpl;
import com.kjt.service.common.dao.ibatis.IBatisDAOException;
import com.kjt.service.common.dao.ibatis.SqlmapUtils;
import com.kjt.service.common.exception.DataAccessException;


@Repository("${name}Helpper")
public class ${name}HelpperIbatisDAOImpl extends AbsHelpperIBatisDAOImpl<${name}Helpper> implements I${name}HelpperDAO<${name}Helpper> {

	@Resource(name = "${masterDataSource}")
	private DataSource masterDataSource;
	
	@Resource(name = "${mapQueryDataSource}")
	private DataSource mapQueryDataSource;
	
	@Override
	public Class<${name}HelpperMapper> getMapperClass() {
		
		return ${name}HelpperMapper.class;
	}
	
	@Override
	public String get$TKjtTabName(String tabNameSuffix) {
	  suffixValidate(tabNameSuffix);
	  StringBuilder tableName = new StringBuilder("${tab.name}");
      if(tabNameSuffix!=null&&tabNameSuffix.trim().length()>0){
        tableName.append("_");
        tableName.append(tabNameSuffix.trim()); 
      }
      return tableName.toString();
    }
  
	@Override
	public DataSource getMasterDataSource(){
		return masterDataSource;
	}
	
	@Override
	public DataSource getMapQueryDataSource(){
		if (mapQueryDataSource == null) {
 			return getMasterDataSource();
 		}
 		return mapQueryDataSource;
	}
	
	@Override
    public List<${name}> queryByHelpper(${name}Helpper helpper, String tabNameSuffix) {
        validate(helpper);

        helpper.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

        SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
        try {

            ${name}HelpperMapper mapper = session.getMapper(getMapperClass());

            return mapper.queryByHelpper(helpper);

        } catch (Exception t) {
            t.printStackTrace();
            throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
        } finally {
            session.commit();
            session.close();
        }
    }
	
}
