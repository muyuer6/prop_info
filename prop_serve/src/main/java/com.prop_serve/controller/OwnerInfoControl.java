package com.prop_serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prop_serve.common.Result;
import com.prop_serve.entity.*;
import com.prop_serve.mapper.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ownerInfo")
public class OwnerInfoControl {
    @Resource
    HouseBindingMapper houseBindingMapper;
    @Resource
    HouseMapper houseMapper;
    @Resource
    ParkBindingMapper parkBindingMapper;
    @Resource
    ParkingLotMapper parkingLotMapper;

    @Resource
    TotalFeeMapper totalFeeMapper;

    @Resource
    PerFeeMapper perFeeMapper;

    @RequestMapping("/getHouse/{idnum}")
    public Result<?> getHouse(@PathVariable String idnum) {
        QueryWrapper<HouseBinding> q1 = new QueryWrapper<>();
        q1.eq("idnum", idnum);
        List<HouseBinding> houseBindingList = houseBindingMapper.selectList(q1);

        List<House> houses = new ArrayList<>();
        houseBindingList.forEach(item -> {
            QueryWrapper<House> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("housenumber", item.getHousenumber());
            houses.add(houseMapper.selectOne(queryWrapper));
        });
        return Result.success(houses);
    }

    //    无分页
    @GetMapping("/getFee/{idnum}")
    public Result<?> getFee(@PathVariable String idnum) {
        QueryWrapper<HouseBinding> q1 = new QueryWrapper<>();
        q1.eq("idnum", idnum);
        HouseBinding houseBinding = houseBindingMapper.selectOne(q1);
        if (houseBinding == null) {
            return Result.success();
        }
        String housenumber = houseBinding.getHousenumber();
        List<PerFee> perFeeList = new ArrayList<>();
        QueryWrapper<PerFee> q2 = new QueryWrapper<>();
        q2.eq("housenumber", housenumber);
        perFeeList = perFeeMapper.selectList(q2);

        Page<PerFee> page = new Page<PerFee>().setRecords(perFeeList);

//        return Result.success(perFeeList);
        return Result.success(page);
    }

    //    分页+获取账单
    @GetMapping("/getFee")
    public Result<?> findPage(@RequestParam(defaultValue = "") String idnum,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "7") Integer pageSize) {

        QueryWrapper<HouseBinding> q1 = new QueryWrapper<>();
        q1.eq("idnum", idnum);
        HouseBinding houseBinding = houseBindingMapper.selectOne(q1);
        if (houseBinding == null) {
            return Result.success();
        }
        String housenumber = houseBinding.getHousenumber();
        QueryWrapper<PerFee> q2 = new QueryWrapper<>();
        q2.eq("housenumber", housenumber);

        Page<PerFee> page = perFeeMapper.selectPage(new Page<>(pageNum, pageSize), q2.orderByDesc("chargetime"));
        return Result.success(page);
    }

    /**
     * setFee
     * perFee是修改(增删改)业主缴费的实例
     * oldTotalFee是通过perFee的chargetime在总账单中找到的表,可能为空
     * type是此次操作的类型,0表示编辑和增加,直接插入,1表示删除数据(也就是在总表中减少对应值)
     */


    TotalFee setTotalFee(PerFee perFee, TotalFee oldTotalFee, int type) {
        TotalFee totalFee = new TotalFee();
        totalFee.setSeleId(oldTotalFee.getSeleId());//避免返回值的主键为空
        totalFee.setChargetime(perFee.getChargetime());
        if (type == 0) {
            //编辑和增加
            totalFee.setTotalf(perFee.getExpectfee() + oldTotalFee.getTotalf());
            totalFee.setProperty(perFee.getProperty() + oldTotalFee.getProperty());
            totalFee.setWater(perFee.getWater() + oldTotalFee.getWater());
            totalFee.setElec(perFee.getElec() + oldTotalFee.getElec());
            totalFee.setGas(perFee.getGas() + oldTotalFee.getGas());
            totalFee.setTv(perFee.getTv() + oldTotalFee.getTv());
            totalFee.setHeating(perFee.getHeating() + oldTotalFee.getHeating());
            totalFee.setPark(perFee.getPark() + oldTotalFee.getPark());
        } else if (type == 1) {
            //删
            totalFee.setTotalf(oldTotalFee.getTotalf() - perFee.getExpectfee());
            totalFee.setProperty(oldTotalFee.getProperty() - perFee.getProperty());
            totalFee.setWater(oldTotalFee.getWater() - perFee.getWater());
            totalFee.setElec(oldTotalFee.getElec() - perFee.getElec());
            totalFee.setGas(oldTotalFee.getGas() - perFee.getGas());
            totalFee.setTv(oldTotalFee.getTv() - perFee.getTv());
            totalFee.setHeating(oldTotalFee.getHeating() - perFee.getHeating());
            totalFee.setPark(oldTotalFee.getPark() - perFee.getPark());
        }
        return totalFee;
    }


    //        增加对总费用表的更新
    public void updateTotalFee(PerFee perFee, int type) {
        QueryWrapper<TotalFee> totalFeeQueryWrapper = new QueryWrapper<>();

        totalFeeQueryWrapper.eq("chargetime", perFee.getChargetime());
        totalFeeMapper.selectOne(totalFeeQueryWrapper);
        TotalFee addTotalFee = new TotalFee();
        if (perFee!=null&&totalFeeMapper.selectOne(totalFeeQueryWrapper) != null) {
            System.out.println("更新");
//
            //查询对应时间有没有过记录
            //能找到数据,就把原来的带上更新,可以是增加或者删除,传入的type应该是0或者1
            addTotalFee = totalFeeMapper.selectOne(totalFeeQueryWrapper);
            System.out.println("赋值后seleId" + addTotalFee.getSeleId());

            addTotalFee = setTotalFee(perFee, addTotalFee, type);
            System.out.println("最后seleId" + addTotalFee.getSeleId());

            totalFeeMapper.updateById(addTotalFee);
        } else {

            System.out.println("首次增加");
            addTotalFee = setTotalFee(perFee, addTotalFee, type);
            totalFeeMapper.insert(addTotalFee);
        }
    }

    //计算单个缴费记录的总金额
    Double calExpectFee(PerFee perFee) {
        double fee = perFee.getProperty()
                + perFee.getWater()
                + perFee.getElec()
                + perFee.getGas()
                + perFee.getTv()
                + perFee.getHeating()
                + perFee.getPark();
        return fee;
    }

    @PostMapping("/addFee")
    public Result<?> addFee(@RequestBody PerFee perFee) {
//        检查时间是否重复
        QueryWrapper<PerFee> q1 = new QueryWrapper<>();
        q1.eq("housenumber", perFee.getHousenumber());
        q1.eq("chargetime", perFee.getChargetime());

        if (perFeeMapper.selectOne(q1) != null) {
            return Result.error("-1", "缴费时间重复");
        }

        perFee.setExpectfee(calExpectFee(perFee));

        perFeeMapper.insert(perFee);
        System.out.println(perFee.getChargetime() + "新增缴费" + perFee.getHousenumber());

        //更新总数据
        updateTotalFee(perFee, 0);

//        System.out.println("缴费时间"+"\n"+perFee.getChargetime());

        return Result.success();
    }

    @RequestMapping("/editFee")
    public Result<?> editFee(@RequestBody PerFee perFee) {
        //        检查时间是否重复
        QueryWrapper<PerFee> q1 = new QueryWrapper<>();
        //        q1.eq("seleId",perFee.getSeleId());
        q1.eq("housenumber", perFee.getHousenumber());
        q1.eq("chargetime", perFee.getChargetime());
        PerFee p = perFeeMapper.selectOne(q1);


        if (p != null &&!p.getSeleId().equals(perFee.getSeleId())) {
            return Result.error("-1", "缴费时间重复");
        }

        perFee.setExpectfee(calExpectFee(perFee));
        //更新总数据
        //编辑了数据,就需要先删除原来的数据,再增加新的数据
        updateTotalFee(p, 1);//先删
        perFeeMapper.updateById(perFee);

        updateTotalFee(perFee, 0);//再增
        System.out.println(perFee.getChargetime() + "编辑缴费" + perFee.getHousenumber());


        return Result.success();
    }

    @DeleteMapping("/deleteSingle/{seleId}")
    public Result<?> deleteSingle(@PathVariable Long seleId) {


        QueryWrapper<PerFee> perFeeQueryWrapper = new QueryWrapper<>();
        perFeeQueryWrapper.eq("seleId", seleId);
        PerFee perFee = perFeeMapper.selectOne(perFeeQueryWrapper);
        updateTotalFee(perFee, 1);

//        先更总表,再更个人表,否则总表找不到数据
        perFeeMapper.deleteById(seleId);

        return Result.success();
    }

}
