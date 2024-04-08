package com.xbb.stock.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户表
 * @TableName sys_user
 */
@Data
@ApiModel(description = "用户基本信息")
public class SysUser implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 账户
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户密码密文
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱(唯一)
     */
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    private Byte status;

    /**
     * 性别(1.男 2.女)
     */
    private Byte sex;

    /**
     * 是否删除(1未删除；0已删除)
     */
    private Byte deleted;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 更新人
     */
    private Long updateId;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    private Byte createWhere;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}