package com.bmw.seckill.model;

import java.util.Date;


/**
 * 
 * 管理员姓名表
 * 
 **/
public class SeckillAdmin{


  /****/

  private Long id;


  /****/

  private String loginName;


  /****/

  private String password;


  /****/

  private String name;


  /****/

  private String ipRange;


  /****/

  private Date createTime;




  public void setId(Long id) {     this.id = id;
  }


  public Long getId() {     return this.id;
  }


  public void setLoginName(String loginName) {     this.loginName = loginName;
  }


  public String getLoginName() {     return this.loginName;
  }


  public void setPassword(String password) {     this.password = password;
  }


  public String getPassword() {     return this.password;
  }


  public void setName(String name) {     this.name = name;
  }


  public String getName() {     return this.name;
  }


  public void setIpRange(String ipRange) {     this.ipRange = ipRange;
  }


  public String getIpRange() {     return this.ipRange;
  }


  public void setCreateTime(Date createTime) {     this.createTime = createTime;
  }


  public Date getCreateTime() {     return this.createTime;
  }

}
