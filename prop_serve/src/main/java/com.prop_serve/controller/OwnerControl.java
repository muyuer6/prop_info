package com.prop_serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prop_serve.common.Result;
import com.prop_serve.entity.House;
import com.prop_serve.entity.HouseBinding;
import com.prop_serve.entity.Owner;
import com.prop_serve.mapper.HouseBindingMapper;
import com.prop_serve.mapper.OwnerMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerControl {
    @Resource
    OwnerMapper ownerMapper;

    Owner owner =new Owner();
    @Resource
    HouseBindingMapper houseBindingMapper;

    //获取用户表
//    @RequestMapping
//    public List<HouseOwner> getOwner() {
//        return houseOwnerMapper.selectList(null);
//    }

//    分页+获取用户表，支持按证件号查找个人
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
//        LambdaQueryWrapper<Owner> wrapper = Wrappers.<Owner>lambdaQuery().like(Owner::getIdnum, search);
        //        查询数据不为空，从第一页开始展示
        if (!search.isEmpty()){
            pageNum=1;
        }

        QueryWrapper<Owner> q=new QueryWrapper<>();
        q.like("idnum", search).or().like("name", search);

        Page<Owner> page = ownerMapper.selectPage(new Page<>(pageNum, pageSize), q);

        return Result.success(page);
    }

//    增加业主
    @PostMapping("/addOwner")
    public Result<?> addOwner(@RequestBody Owner newOwner) {
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idnum", newOwner.getIdnum());

        owner = ownerMapper.selectOne(queryWrapper);
        if (owner != null) {
            return Result.error("-1", "重复的业主证件号!");
        }

        ownerMapper.insert(newOwner);
        System.out.println("新增业主" + newOwner.getIdnum());
        return Result.success();
    }


//    由于以证件号为主键，在这个方法里主键不能修改，这样就使得这个功能不完善
//        除非另设主键，同时设置证件号为unique，使得证件号不能重复
    @PutMapping("/editOwner")
    public Result<?> editOwner(@RequestBody Owner owner) {
//        先看有没有绑定房屋，如果有就阻止
        QueryWrapper<HouseBinding> q1=new QueryWrapper<>();
        q1.eq("idnum",owner.getIdnum());
//        能在房屋绑定表中找到数据，就阻止
        if (houseBindingMapper.selectOne(q1)!=null){
            return Result.error("-1", "请先解除当前业主的房屋绑定!");
        }

//        如果把身份证改成了另一人的，要阻止
//        先按seleId找到原来数据，再进行对比
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idnum", owner.getIdnum());
        Owner h = ownerMapper.selectOne(queryWrapper);
        //如果新证件已经被注册过 且注册者不是自己，就阻止
        if (h != null&&h.getSeleId()!=owner.getSeleId()) {
            return Result.error("-1", "重复的业主证件号!");
        }
        ownerMapper.updateById(owner);
        System.out.println("更新业主" + owner.getIdnum());
        return Result.success();
    }

//   也可以换成，传入两个数据，把原来的数据删除，再新增一个数据，但这样增加了系统负担
//    @PutMapping("/editOwner")
//    public Result<?> editOwner(@RequestBody HouseOwner newOwner) {
//
//        houseOwnerMapper.updateById(newOwner);
//        System.out.println("更新业主" + newOwner.getIdnum());
//        return Result.success();
//    }

//    单个删除用户
//    直接用housenumber就行，返回全部数据效率反而低
    @DeleteMapping("/deleteSingle/{idnum}")
    public Result<?> deleteSingle(@PathVariable String idnum) {
        //        先看有没有绑定房屋，如果有就阻止
        QueryWrapper<HouseBinding> q1=new QueryWrapper<>();
        q1.eq("idnum",idnum);
//        能在房屋绑定表中找到数据，就阻止
        if (houseBindingMapper.selectOne(q1)!=null){
            return Result.error("-1", "请先解除当前业主的房屋绑定!");
        }

        QueryWrapper<Owner> q2=new QueryWrapper<>();
        q2.eq("idnum",idnum);
        ownerMapper.delete(q2);
        System.out.println("删除业主" + idnum);
        return Result.success();
    }

//    批量删除业主
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<String> idnum) {
        //  删除业主前，检查是否还存在绑定房屋，如果有，则不能删除
        for (String s : idnum) {
            QueryWrapper<HouseBinding> q=new QueryWrapper<>();
            q.eq("idnum",s);
            HouseBinding h=houseBindingMapper.selectOne(q);
            if (h!=null){
                String s1="删除失败，请先解除业主"+ owner.getIdnum()+"的房屋绑定！";
                return Result.error("-1",s1);
            }
            QueryWrapper<Owner> q1=new QueryWrapper<>();
            q1.eq("idnum",s);
            ownerMapper.delete(q1);
        }

        System.out.println("批量删除业主");
        return Result.success();
    }


}
