package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("parkbinding")
public class ParkBinding {
    @TableId(value="parking")
    private String parking;
    private String housenumber;
    private int occupation;
}

