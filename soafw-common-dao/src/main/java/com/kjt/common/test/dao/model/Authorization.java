package com.kjt.common.test.dao.model;

import java.util.HashMap;
import java.util.Map;

import com.kjt.service.common.annotation.JField;
import com.kjt.service.common.dao.IModel;

public class Authorization implements IModel {

  private static final long serialVersionUID = 1L;

  static Map<String, Integer> fks = new HashMap<String, Integer>();

  static {
    /**
     * 把该model相关的外键属性字段注册到fks map中
     */
    // eg:fks.put("cityId",0);
  }

  /**
   * 是否是外键字段
   */
  public static boolean isFk(String name) {
    return fks.containsKey(name);
  }

  /**
	 * 
	 */
  @JField(name = "ID")
  protected Integer id;

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return this.id;
  }

  /**
	 * 
	 */
  @JField(name = "mallcode")
  private String mallcode;

  public void setMallcode(String mallcode) {
    this.mallcode = mallcode;
  }

  public String getMallcode() {
    return this.mallcode;
  }

  /**
	 * 
	 */
  @JField(name = "mallname")
  private String mallname;

  public void setMallname(String mallname) {
    this.mallname = mallname;
  }

  public String getMallname() {
    return this.mallname;
  }

  /**
	 * 
	 */
  @JField(name = "appid")
  private String appid;

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getAppid() {
    return this.appid;
  }

  /**
	 * 
	 */
  @JField(name = "appsecret")
  private String appsecret;

  public void setAppsecret(String appsecret) {
    this.appsecret = appsecret;
  }

  public String getAppsecret() {
    return this.appsecret;
  }

  /**
	 * 
	 */
  @JField(name = "assigndate")
  private java.sql.Date assigndate;

  public void setAssigndate(java.sql.Date assigndate) {
    this.assigndate = assigndate;
  }

  public java.sql.Date getAssigndate() {
    return this.assigndate;
  }

  /**
	 * 
	 */
  @JField(name = "expiredate")
  private java.sql.Date expiredate;

  public void setExpiredate(java.sql.Date expiredate) {
    this.expiredate = expiredate;
  }

  public java.sql.Date getExpiredate() {
    return this.expiredate;
  }

  /**
   * appid...1-.. 0-..
   */
  @JField(name = "status")
  private Integer status;

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getStatus() {
    return this.status;
  }

  /**
   * 保存时非空数据项校验；
   */
  public boolean validate() {
    boolean passed = true;
    return passed;
  }

  /**
   * 保存时对应的分表；
   */
  private String tKjtTabName;

  public String getTKjtTabName() {
    return tKjtTabName;
  }

  @Override
  public void setTKjtTabName(String tKjtTabName) {
    this.tKjtTabName = tKjtTabName;
  }
}
