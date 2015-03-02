package com.kjt.common.test.dao;

import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IFKDAO;
import com.kjt.service.common.dao.IIDAO;
import com.kjt.common.test.dao.model.Authorization;

public interface IAuthorizationDAO<T extends Authorization> extends IIDAO<T>,IFKDAO<T>,IDAO<T> {

}
