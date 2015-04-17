package com.kjt.service.common.impl;

import com.kjt.service.common.IService;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.result.IResult;

public abstract class AbsServiceImpl<T extends IResult> implements IService<T> {
	/**
	 * Logger for this class
	 */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public AbsServiceImpl(){
	    
	}

	public String hello(String name){
		if (logger.isInfoEnabled()) {
			logger.info("hello(String name={}) - start", name); //$NON-NLS-1$
		}

		String returnString = "hello," + name;

		if (logger.isInfoEnabled()) {
			logger.info("hello(String name={}) - end - return value={}", name, returnString); //$NON-NLS-1$
		}
		return returnString;
	}

}
