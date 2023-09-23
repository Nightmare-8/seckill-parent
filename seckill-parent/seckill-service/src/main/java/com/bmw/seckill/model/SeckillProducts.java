package com.bmw.seckill.model;

import java.util.Date;


/**
 * 
 * 商品表
 * 
 **/
public class SeckillProducts{


  /**主键**/

  private Long id;


  /**商品名称**/

  private String name;


  /**商品总库存数量**/

  private Integer count;


  /**已售数量**/

  private Integer saled;


  /**创建时间**/

  private Date createTime;


  /**是否删除：0否 1是**/

  private Integer isDeleted;


  /****/

  private Date startBuyTime;


  /****/

  private Date updatedTime;


  /****/

  private String productDesc;


  /****/

  private Integer status;


  /****/

  private String memo;


  /**唯一标识key**/

  private String productPeriodKey;




  public void setId(Long id) {     this.id = id;
  }


  public Long getId() {     return this.id;
  }


  public void setName(String name) {     this.name = name;
  }


  public String getName() {     return this.name;
  }


  public void setCount(Integer count) {     this.count = count;
  }


  public Integer getCount() {     return this.count;
  }


  public void setSaled(Integer saled) {     this.saled = saled;
  }


  public Integer getSaled() {     return this.saled;
  }


  public void setCreateTime(Date createTime) {     this.createTime = createTime;
  }


  public Date getCreateTime() {     return this.createTime;
  }


  public void setIsDeleted(Integer isDeleted) {     this.isDeleted = isDeleted;
  }


  public Integer getIsDeleted() {     return this.isDeleted;
  }


  public void setStartBuyTime(Date startBuyTime) {     this.startBuyTime = startBuyTime;
  }


  public Date getStartBuyTime() {     return this.startBuyTime;
  }


  public void setUpdatedTime(Date updatedTime) {     this.updatedTime = updatedTime;
  }


  public Date getUpdatedTime() {     return this.updatedTime;
  }


  public void setProductDesc(String productDesc) {     this.productDesc = productDesc;
  }


  public String getProductDesc() {     return this.productDesc;
  }


  public void setStatus(Integer status) {     this.status = status;
  }


  public Integer getStatus() {     return this.status;
  }


  public void setMemo(String memo) {     this.memo = memo;
  }


  public String getMemo() {     return this.memo;
  }


  public void setProductPeriodKey(String productPeriodKey) {     this.productPeriodKey = productPeriodKey;
  }


  public String getProductPeriodKey() {     return this.productPeriodKey;
  }

}
