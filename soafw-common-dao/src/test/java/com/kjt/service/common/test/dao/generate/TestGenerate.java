package com.kjt.service.common.test.dao.generate;

import org.junit.Test;

import com.kjt.service.common.dao.generate.tool.DaoGenFromDB;

public class TestGenerate {

	@Test
	public void test(){
		try {
			DaoGenFromDB.generateDAO("oper_db", "authorization", "classpath:springtest.xml", "com.kjt.common.test", "src/main/java/", "src/main/resources/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
