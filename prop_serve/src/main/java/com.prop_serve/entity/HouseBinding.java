package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("housebinding")
public class HouseBinding {
    @TableId(value = "idnum")
    private  String idnum;
    private String housenumber;
}
