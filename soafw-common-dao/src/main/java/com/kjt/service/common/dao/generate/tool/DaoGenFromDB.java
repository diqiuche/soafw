package com.kjt.service.common.dao.generate.tool;

import javax.sql.DataSource;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kjt.service.common.dao.generate.config.TemplateFactoryBean;

import freemarker.template.Configuration;

public class DaoGenFromDB extends DaoGen {
  private String table;
  private String dbName;
  private DataSource[] datasource;

  public DaoGenFromDB(DataSource[] datasource, String dbName, String table) {
    this.datasource = datasource;
    this.dbName = dbName;
    this.table = table;
  }

  @Override
  protected DB load() throws Exception {
    return new DB(dbName, table, datasource);
  }

  /**
   * sqlserver database 生成dao文件
   * 
   * @param masterDataSourceBean
   *          主数据源bean名称
   * @param slaveDataSourceBean
   *          从数据源bean名称
   * @param dbName
   *          db名称
   * @param tableName
   *          表名
   * @param springXml
   *          spring文件
   * @param packageName
   *          包名
   * @param targetJava
   *          存放产生java的目录
   * @param resources
   *          存放产生xml的目录
   * @throws Exception
   */
  public static void generateSQLSvrDAO(String dbName, String tableName, String springXml,
      String packageName, String targetJava, String resources) throws Exception {
      DBSetting.setSetting("type","sqlserver");
    generateDAO(dbName, tableName, springXml, packageName, targetJava, resources);
  }

  public static void generateSQLSvrDAOWithHelpper(String dbName, String tableName,
      String springXml, String packageName, String targetJava, String resources) throws Exception {
    DBSetting.setSetting("type","sqlserver");
    DBSetting.setGenHelp();
    generateDAO(dbName, tableName, springXml, packageName, targetJava, resources);
  }

  public static void generateDAOWithHellper(String dbName, String tableName, String springXml,
      String packageName, String targetJava, String resources) throws Exception {
    DBSetting.setGenHelp();
    generateDAO(dbName, tableName, springXml, packageName, targetJava, resources);
  }

  /**
   * mysql database 生成dao文件
   * 
   * @param masterDataSourceBean
   *          主数据源bean名称
   * @param slaveDataSourceBean
   *          从数据源bean名称
   * @param dbName
   *          db名称
   * @param tableName
   *          表名
   * @param springXml
   *          spring文件
   * @param packageName
   *          包名
   * @param targetJava
   *          存放产生java的目录
   * @param resources
   *          存放产生xml的目录
   * @throws Exception
   */
  public static void generateDAO(String dbName, String tableName, String springXml,
      String packageName, String targetJava, String resources) throws Exception {
    TemplateFactoryBean templateFactoryBean = new TemplateFactoryBean();
    templateFactoryBean.setPath("classpath:/META-INF/generate/templates/dal");

    ClassPathXmlApplicationContext cac = new ClassPathXmlApplicationContext(
        springXml != null ? springXml : "generateDAO.xml");
    String masterDataSourceBean = dbName;
    String slaveDataSourceBean = dbName + "_slave";
    String mapQueryDataSourceBean = dbName + "_map_query";
    DataSource masterDataSource = (DataSource) cac.getBean(masterDataSourceBean);
    DataSource[] datasoures = { masterDataSource };
    DaoGenFromDB hf = new DaoGenFromDB(datasoures, masterDataSourceBean, tableName);
    hf.setConfiguration((Configuration) templateFactoryBean.getObject());
    hf.process(masterDataSourceBean, slaveDataSourceBean, mapQueryDataSourceBean, packageName,
        targetJava, resources);
  }

}
