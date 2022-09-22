package com.prop_serve.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test")
public class Test {
    String str;
}
