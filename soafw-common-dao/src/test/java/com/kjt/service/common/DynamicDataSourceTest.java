package com.kjt.service.common;

import java.sql.Connection;

import org.junit.Test;

import com.kjt.service.common.datasource.DynamicDataSource;

public class DynamicDataSourceTest {

	@Test
	public void getConn() {
		DynamicDataSource datasource = new DynamicDataSource();
		datasource.setPrefix("ACTION");
		datasource.init();
		for (int i = 0; i < 5; i++) {
			Connection conn;
			try {
				conn = datasource.getConnection();
				System.out.println(conn);
				conn.close();
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
