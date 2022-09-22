package com.prop_serve.controller;


import com.prop_serve.entity.Test;
import com.prop_serve.mapper.TestMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestControl {
    @Resource
    TestMapper testMapper;

    @RequestMapping
    public List<Test> getTest() {
        return testMapper.selectList(null);
    }

    @PostMapping
    public String addTest( @RequestBody Test test){
        testMapper.insert(test);
        return "success"+test;
    }
//    public String addTest(@PathVariable String a){
//        Test test=new Test();
//        test.setStr(a);
//        testMapper.insert(test);
//        return a;
//    }

}
