package com.kjt.common.test.dao;

import com.kjt.common.test.dao.model.Authorization;
import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IFKDAO;
import com.kjt.service.common.dao.ISDAO;

public interface IAuthorizationDAO<T extends Authorization> extends ISDAO<T>,IFKDAO<T>,IDAO<T> {

}
