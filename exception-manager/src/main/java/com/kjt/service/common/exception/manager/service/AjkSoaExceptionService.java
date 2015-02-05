package com.kjt.service.common.exception.manager.service;

import com.kjt.service.common.exception.manager.dao.AjkExceptionDao;
import com.kjt.service.common.exception.manager.dao.AjkSoaSpDao;
import com.kjt.service.common.exception.manager.model.AjkException;

import java.util.List;

/**
 * Created by kevin on 15/1/13.
 */
public class AjkSoaExceptionService {

    private AjkExceptionDao ajkExceptionDao = new AjkExceptionDao();
    private AjkSoaSpDao ajkSoaSpDao =new AjkSoaSpDao();
    public List<AjkException> list(int code){
        List<AjkException> list =ajkExceptionDao.list(code);
        for(AjkException ajkException:list){
            ajkException.setAjkSoaSp(ajkSoaSpDao.get(ajkException.getSpid()));
        }
        return list;
    }
}
