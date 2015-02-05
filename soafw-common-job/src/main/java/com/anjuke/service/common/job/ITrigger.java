package com.anjuke.service.common.job;

public interface ITrigger {
	
	public IJob getJobDetail();
	
	public String getCronExpression();
	
}
