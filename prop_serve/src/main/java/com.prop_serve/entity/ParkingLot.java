package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("parkinglot")
public class ParkingLot {
    @TableId(value="seleId",type = IdType.AUTO)
    private Long seleId;//作为选择数据的标记
    private String parking;//停车位编号
    private int occupation;//使用情况， 0表可租或可售，1表已租，2表已售出


}
