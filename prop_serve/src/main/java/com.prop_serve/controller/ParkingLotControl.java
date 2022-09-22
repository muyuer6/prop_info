package com.prop_serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prop_serve.common.Result;
import com.prop_serve.entity.House;
import com.prop_serve.entity.HouseBinding;
import com.prop_serve.entity.ParkBinding;
import com.prop_serve.entity.ParkingLot;
import com.prop_serve.mapper.ParkBindingMapper;
import com.prop_serve.mapper.ParkingLotMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/parking")
public class ParkingLotControl {

    @Resource
    ParkingLotMapper parkingLotMapper;
    @Resource
    ParkBindingMapper parkBindingMapper;

    ParkingLot parkingLot = new ParkingLot();


    //    @RequestMapping
//    public List<ParkingLot> getOwner() {
//        return ParkingLotMapper.selectList(null);
//    }
//
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        //        查询数据不为空，从第一页开始展示
        if (!search.isEmpty()){
            pageNum=1;
        }

        LambdaQueryWrapper<ParkingLot> wrapper = Wrappers.<ParkingLot>lambdaQuery().like(ParkingLot::getParking, search);
        Page<ParkingLot> page = parkingLotMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(page);
    }


    @PostMapping("/addParking")
    public Result<?> addOwner(@RequestBody ParkingLot newParking) {
        QueryWrapper<ParkingLot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking", newParking.getParking());

        parkingLot = parkingLotMapper.selectOne(queryWrapper);
        if (parkingLot != null) {
            return Result.error("-1", "重复的车位号!");
        }

        parkingLotMapper.insert(newParking);
        System.out.println("新增车位" + newParking.getParking());
        return Result.success();
    }

    @PutMapping("/editParking")
    public Result<?> editParking(@RequestBody ParkingLot newParking) {
        QueryWrapper<ParkBinding> q1 = new QueryWrapper<>();
        q1.eq("parking", newParking.getParking());
        ParkBinding parkBinding = parkBindingMapper.selectOne(q1);
        if (parkBinding != null) {
            return Result.error("-1", "请先解除当前车位的房屋绑定!");
        }


        QueryWrapper<ParkingLot> q2 = new QueryWrapper<>();
        q2.eq("parking", newParking.getParking());
        parkingLot = parkingLotMapper.selectOne(q2);

        //如果新房号已经被注册过 且注册者不是自己，就阻止
        if (parkingLot != null && parkingLot.getSeleId() != newParking.getSeleId()) {
            return Result.error("-1", "重复的车位号!");
        }
        parkingLotMapper.updateById(newParking);
        System.out.println("更新车位" + newParking.getParking());
        return Result.success();
    }


    @DeleteMapping("/deleteSingle/{parking}")
    public Result<?> deleteSingle(@PathVariable String parking) {
        QueryWrapper<ParkBinding> q1 = new QueryWrapper<>();
        q1.eq("parking", parking);
        ParkBinding parkBinding = parkBindingMapper.selectOne(q1);
        if (parkBinding != null) {
            return Result.error("-1", "请先解除当前车位的房屋绑定!");
        }

        QueryWrapper<ParkingLot> q2 = new QueryWrapper<>();
        q2.eq("parking", parking);
        parkingLotMapper.delete(q2);
        System.out.println("删除车位" + parking);
        return Result.success();
    }

    //    批量删除房屋车位绑定
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<String> parking) {
//  删除房屋前，检查是否还存在业主，如果有，则不能删除
        for (String p : parking) {
            QueryWrapper<ParkBinding> q = new QueryWrapper<>();
            q.eq("parking", p);


            if (parkBindingMapper.selectOne(q) != null) {
                String s = "删除失败，请先解除车位" + p + "的房屋绑定！";
                return Result.error("-1", s);
            }
            QueryWrapper<ParkingLot> q1 = new QueryWrapper<>();
            q1.eq("parking", p);
            parkingLotMapper.delete(q1);

        }
        System.out.println("批量删除业主");
        return Result.success();
    }
}
