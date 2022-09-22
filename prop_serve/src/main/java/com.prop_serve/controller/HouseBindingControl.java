package com.prop_serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prop_serve.common.Result;
import com.prop_serve.entity.House;
import com.prop_serve.entity.HouseBinding;
import com.prop_serve.entity.Owner;
import com.prop_serve.mapper.HouseBindingMapper;
import com.prop_serve.mapper.HouseMapper;
import com.prop_serve.mapper.OwnerMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/houseBinding")
public class HouseBindingControl {
    @Resource
    HouseBindingMapper houseBindingMapper;
    @Resource
    OwnerMapper ownerMapper;

    @Resource
    HouseMapper houseMapper ;

    //获取当前房屋业主信息
    @GetMapping("/getOwner/{housenumber}")
    public Result<?> getOwner(@PathVariable String housenumber) {

        //先通过传入的housenumber在绑定表中找到对应的行
        QueryWrapper<HouseBinding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("housenumber", housenumber);
        List<HouseBinding> bindinglist= houseBindingMapper.selectList(queryWrapper);

//      多表查询，用bindinglist中housenumber对应的idnum从Owner中查找业主
        List<Owner> ownerList=new ArrayList<>();
        bindinglist.forEach(item->{
            QueryWrapper<Owner> q=new QueryWrapper<>();
            q.eq("idnum",item.getIdnum());
            ownerList.add(ownerMapper.selectOne(q));
        });

        return Result.success(ownerList);
    }

    //绑定业主
    @RequestMapping("/addOwner/{housenumber}/{idnum}")
    public Result<?> addOwner(@PathVariable String housenumber, @PathVariable String idnum){
//        先看有没有这个人
        QueryWrapper<Owner> q1=new QueryWrapper<>();
        q1.eq("idnum",idnum);
        Owner owner = ownerMapper.selectOne(q1);

        if (owner ==null){
            return Result.error("-1","无效的证件号，请先检查输入或注册业主");
        }

        //再看是否已经绑定过了,idnum为主键，每个人限制一套
        QueryWrapper<HouseBinding> q2 = new QueryWrapper<>();
        q2.eq("idnum",idnum);

        HouseBinding houseBinding = houseBindingMapper.selectOne(q2);
        if (houseBinding !=null){
            return Result.error("-1","当前业主已有绑定房屋");
        }


        //进行绑定
        HouseBinding binding=new HouseBinding();
        binding.setHousenumber(housenumber);
        binding.setIdnum(idnum);
        houseBindingMapper.insert(binding);


//        房屋绑定数+1
        QueryWrapper<House> q3=new QueryWrapper<>();
        q3.eq("housenumber",housenumber);
        House house= houseMapper.selectOne(q3);
        house.setHnum(house.getHnum()+1);
        houseMapper.updateById(house);

        return Result.success();

    }

    //删除业主
    @DeleteMapping("/deleteOwner/{housenumber}/{idnum}")
    public Result<?> deleteOwner(@PathVariable String housenumber, @PathVariable String idnum){
//        查找
        QueryWrapper<HouseBinding> q1 = new QueryWrapper<>();
        q1.eq("idnum",idnum);

//        删除
        houseBindingMapper.delete(q1);
        System.out.println("解绑");

        //        房屋绑定业主数-1
        QueryWrapper<House> q2=new QueryWrapper<>();
        q2.eq("housenumber",housenumber);
        House house= houseMapper.selectOne(q2);

        house.setHnum(house.getHnum()-1);
        houseMapper.updateById(house);

        return Result.success();
    }
}
