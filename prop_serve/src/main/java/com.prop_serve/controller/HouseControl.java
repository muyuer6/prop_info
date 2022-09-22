package com.prop_serve.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prop_serve.common.Result;
import com.prop_serve.entity.HouseBinding;
import com.prop_serve.entity.ParkBinding;
import com.prop_serve.mapper.HouseMapper;
import com.prop_serve.entity.House;
import com.prop_serve.mapper.HouseBindingMapper;
import com.prop_serve.mapper.ParkBindingMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseControl {

    @Resource
    HouseMapper houseMapper;
    @Resource
    HouseBindingMapper houseBindingMapper;
    @Resource
    ParkBindingMapper parkBindingMapper;
    House house = new House();

    //   获取所有房屋信息
//    @RequestMapping
//    public List<House> getHouse(){
//        List<House> list= houseMapper.selectList(null);
//        return houseMapper.selectList(null);
//    }
//    public Result<?> getHouse() {
//        List<House> list = houseMapper.selectList(null);
//        return Result.success(list);
//    }

    //    分页+获取房屋表，支持按房屋编号查找房屋
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
    //      查询数据不为空，从第一页开始展示
        LambdaQueryWrapper<House> wrapper=Wrappers.<House>lambdaQuery();
        if (!search.isEmpty()){
            pageNum=1;
            if (search.equals("0")){
                wrapper.like(House::getHnum,0);
            }
            else wrapper.like(House::getHousenumber, search);
        }

        Page<House> HousePage = houseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(HousePage);
    }

    @PostMapping("/addHouse")
    public Result<?> addHouse(@RequestBody House house) {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("housenumber", house.getHousenumber());

        House h = houseMapper.selectOne(queryWrapper);
        if (h != null) {
            return Result.error("-1", "当前位置房屋已存在!");
        }

        houseMapper.insert(house);
        System.out.println("新增房屋" + house.getHousenumber());
        return Result.success();
    }


    @PutMapping("/editHouse")
    public Result<?> editHouse(@RequestBody House house) {
        /**如果当前房屋有业主，直接从前端拒绝编辑？
         * （X）行不通，因为houseTable没有业主信息，还是要查询
         * （√）可行，houseTable不是有个属性Hnum吗？只要数据合理，hnum就是正确的。
         * 但是后端还是保留这个功能吧，做个保险
         */
        //        存在业主，不可编辑，先解绑
        QueryWrapper<HouseBinding> q1 = new QueryWrapper<>();
        q1.eq("housenumber", house.getHousenumber());
        if (houseBindingMapper.selectOne(q1) != null) {
            Result.error("-1", "编辑前先解绑业主信息！");
        }

        QueryWrapper<ParkBinding> q2 = new QueryWrapper<>();
        q2.eq("housenumber", house.getHousenumber());
        if (parkBindingMapper.selectOne(q2)!=null) {
            return Result.error("-1", "编辑前先解绑车位信息！");
        }


//        如果把房屋改成了另一家的位置，还是要阻止
//        先按seleId找到原来数据，再进行对比
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("housenumber", house.getHousenumber());
        House h = houseMapper.selectOne(queryWrapper);
        //如果新房号已经被注册过 且注册者不是自己，就阻止
        if (h != null && h.getSeleId() != house.getSeleId()) {
            return Result.error("-1", "当前位置房屋已存在！");
        }

        houseMapper.updateById(house);
        System.out.println("更新房屋" + house.getHousenumber());
        return Result.success();
    }


    @DeleteMapping("/deleteSingle/{seleId}")
    public Result<?> deleteSingle(@PathVariable Long seleId) {

        //  删除房屋前，检查是否还存在业主，如果有，则不能删除
//        这也可以从前端限制，但后端还是先保留
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seleId", seleId);
        house = houseMapper.selectOne(queryWrapper);
        if (house.getHnum() != 0) {
            return Result.error("-1", "删除失败，当前房屋有业主");
        }

        houseMapper.deleteById(seleId);
        return Result.success();
    }

    /**
     * 批量删除数据接口
     * 批量删除中，参数是用requestbody注解，mapping只能用post而不能是delete，注意前端接口也是写post
     */
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> seleId) {

        /**此处可以通过房屋位置编号housenumber来批量删除，因为和其他房屋没有冲突关系？
         * 错！因为housenumber不是主键，不适用于deleteBatchIds()方法
         */

        //  删除房屋前，检查是否还存在业主，如果有，则不能删除
        for (int i = 0; i < seleId.size(); i++) {
            QueryWrapper<House> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("seleId", seleId.get(i));
            house = houseMapper.selectOne(queryWrapper);
            if (house.getHnum() != 0) {

                String s = "删除失败，房屋" + house.getHousenumber() + "有业主";
                return Result.error("-1", s);
            }
        }

        houseMapper.deleteBatchIds(seleId);
        System.out.println("批量删除房屋");
        return Result.success();
    }
}

