package com.bmw.seckill.model;

import java.util.Date;


/**
 * 
 * 订单表
 * 
 **/
public class SeckillOrder{


  /**主键**/

  private Long id;


  /**用户id**/

  private Long userId;


  /**商品id**/

  private Long productId;


  /**商品名称**/

  private String productName;


  /**创建时间**/

  private Date createTime;




  public void setId(Long id) {     this.id = id;
  }


  public Long getId() {     return this.id;
  }


  public void setUserId(Long userId) {     this.userId = userId;
  }


  public Long getUserId() {     return this.userId;
  }


  public void setProductId(Long productId) {     this.productId = productId;
  }


  public Long getProductId() {     return this.productId;
  }


  public void setProductName(String productName) {     this.productName = productName;
  }


  public String getProductName() {     return this.productName;
  }


  public void setCreateTime(Date createTime) {     this.createTime = createTime;
  }


  public Date getCreateTime() {     return this.createTime;
  }

}
