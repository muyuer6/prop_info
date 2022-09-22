package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("perfee")
public class PerFee {
    @TableId(value="seleId",type = IdType.AUTO)
    private Long seleId;//作为选择数据的标记
    private String housenumber;
    private double property;//物业费
    private double water;//水
    private double heating;//暖气
    private double elec;//电
    private double gas;//天然气
    private double tv;//电视
    private double park;//停车
    private double expectfee;//预计应该缴纳

    //没有格式化注解,前端会报错
    @DateTimeFormat(pattern = "yyyy-MM")//用于将前端传来的字符串类型转化为Date类型
    @JsonFormat(pattern = "yyyy-MM",timezone="GMT+8")//用于将Date类型格式化为字符串类型返回给前端
    private Date chargetime;//缴费时间
}
