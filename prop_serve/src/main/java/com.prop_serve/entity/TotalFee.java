package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("totalfee")
public class TotalFee {
    @TableId(value="seleId",type = IdType.AUTO)
    private Long seleId;//作为选择数据的标记

    private double property;//物业费
    private double water;//水
    private double heating;//暖气
    private double elec;//电
    private double gas;//天然气
    private double tv;//电视
    private double park;//停车
    private double totalf;//实际总收入


    //没有格式化注解,前端会报错
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone="GMT+8")
    private Date chargetime;//缴费时间
}
