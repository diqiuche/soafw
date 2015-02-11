package com.kjt.common.test.dao;

import com.kjt.common.test.dao.model.Authorization;
import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IFKDAO;
import com.kjt.service.common.dao.IIDAO;

public interface IAuthorizationDAO<T extends Authorization> extends IIDAO<T>,IFKDAO<T>,IDAO<T> {

}
