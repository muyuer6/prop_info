package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("owner")
public class Owner {
    @TableId(value="seleId",type = IdType.AUTO)
    private Long seleId;//作为选择数据的标记

    private String idnum;//身份证
    private String name;//姓名
    private int gender;//性别
    private String phone;//联系方式
    private String work;//工作单位
    private int family;//家庭人数
//    不要了
//    @TableField("houseProp")
//    private int houseProp;//名下房产数
//    @TableField("parkProp")
//    private int parkProp;//名下私有车位数
}
