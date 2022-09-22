package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("house")
public class House {
    @TableId(value="seleId",type = IdType.AUTO)
    private Long seleId;//作为选择数据的标记

    //房屋编号（housenumber：building大楼、unit单元、floor楼层、door门牌号: 实际格式\_b_u_f_d）
    private String housenumber;//_b_u_f_d
    @TableField(value = "h_building")
    private String building;
    @TableField(value = "h_unit")
    private String unit;
    @TableField(value = "h_floor")
    private String floor;
    @TableField(value = "h_door")
    private String door;
    private String housetype;//户型 _r_h_t  room_hall_toilet
    private float area;//面积

    //车位
    private String parking;//停车位编号
    private int occupation;//使用情况， 0表可租或可售，1表已租，2表已售出
//    （大于0时说明被购入，等于0则表示处于空闲状态，hnum）
    private int hnum;//户主人数
}
