package ${package}.dao.ibatis.mapper;

import java.util.List;

import ${package}.dao.model.${name};
import ${package}.dao.model.${name}Helpper;
import com.kjt.service.common.dao.ibatis.IHelpperMapper;

public interface ${name}HelpperMapper extends IHelpperMapper<${name}Helpper>{
	public List<${name}> queryByHelpper(${name}Helpper helpper);
}
