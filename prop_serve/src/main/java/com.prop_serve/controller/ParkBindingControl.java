package com.prop_serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prop_serve.common.Result;
import com.prop_serve.entity.*;
import com.prop_serve.mapper.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/parkBinding")
public class ParkBindingControl {
    @Resource
    ParkBindingMapper parkBindingMapper;
    @Resource
    ParkingLotMapper parkingLotMapper;
    @Resource
    OwnerMapper ownerMapper;

    @Resource
    HouseMapper houseMapper;
    @Resource
    HouseBindingMapper houseBindingMapper;

    /**
     * 车位表查看业主
     * 但车位绑定房屋而不是业主，所以要先找到房屋编号，再找到业主
     * 写两个接口，另一个返回绑定的房屋号，一个返回用户表（看能不能用写好的）
     */

    @GetMapping("/getHouse/{parking}")
    public Result<?> getHouse(@PathVariable String parking) {

////        这样写会报错，因为可能不存在这个数据
//        //通过传入的parking在绑定表中找到对应的行,返回houseNumber
//        ParkBinding binding = parkBindingMapper.selectById(parking);
////        所以要提前返回空值，当然也可以用QueryWrapper+parkBindingMapper+list的形式
//        //投降了，前端格式不太行
//        if (binding==null){
//            return Result.success();
//        }
//        String houseNumber = binding.getHousenumber();
//
//        QueryWrapper<House> q1 = new QueryWrapper<>();
//        q1.eq("housenumber", houseNumber);
//
//        //返回一个house的表，便于后续的解绑操作
//        List<House> house = houseMapper.selectList(q1);
        QueryWrapper<ParkBinding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking", parking);
        List<ParkBinding> bindinglist = parkBindingMapper.selectList(queryWrapper);

//      多表查询，用bindinglist中housenumber对应的idnum从Owner中查找业主
        List<House> list = new ArrayList<>();
        bindinglist.forEach(item -> {
            QueryWrapper<House> q = new QueryWrapper<>();
            q.eq("housenumber", item.getHousenumber());
            list.add(houseMapper.selectOne(q));
        });
        return Result.success(list);
    }

    @GetMapping("/getOwner/{parking}")
    public Result<?> getOwner(@PathVariable String parking) {
        //通过传入的parking在绑定表中找到对应的行,取出houseNumber
        //再用housenumber在房屋绑定表中找业主idnum
        //最后用idnum找到业主信息

        QueryWrapper<ParkBinding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking", parking);
        ParkBinding parkBinding = parkBindingMapper.selectOne(queryWrapper);
        List<Owner> owners = new ArrayList<>();
        if (parkBinding != null) {
            // 用ParkBinding中的housenumber到HouseBinding
            QueryWrapper<HouseBinding> q1 = new QueryWrapper<>();
//        System.out.println("housenumber" + parkBinding.getHousenumber());
            q1.eq("housenumber", parkBinding.getHousenumber());
//      同一间房有多个业主
            List<HouseBinding> houseBinding = houseBindingMapper.selectList(q1);

            for (HouseBinding binding : houseBinding) {
                QueryWrapper<Owner> q2 = new QueryWrapper<>();
                q2.eq("idnum", binding.getIdnum());
                owners.add(ownerMapper.selectOne(q2));
            }

        }

        return Result.success(owners);
    }

    //    增加房屋与车位的绑定，测试有点反直觉
//    addHousePark
    @RequestMapping("/addHP")
    public Result<?> addOwner(@RequestBody ParkBinding newBinding) {

        //        先看有没有这个房子
        QueryWrapper<House> q = new QueryWrapper<>();
        q.eq("housenumber", newBinding.getHousenumber());
        House house = houseMapper.selectOne(q);
        if (house == null) {
            return Result.error("-1", "无效的房屋编号，请先检查输入或注册房屋");
        }
//        更新house信息
        house.setParking(newBinding.getParking());
        house.setOccupation(newBinding.getOccupation());
        houseMapper.updateById(house);

        //进行绑定
        parkBindingMapper.insert(newBinding);

        //更新车位的信息
        QueryWrapper<ParkingLot> q1 = new QueryWrapper<>();
        q1.eq("parking", newBinding.getParking());
        ParkingLot parkingLot = parkingLotMapper.selectOne(q1);
        parkingLot.setOccupation(newBinding.getOccupation());
        //然后update车位
        parkingLotMapper.updateById(parkingLot);
        System.out.println("车位绑定");
        return Result.success();
    }

    @RequestMapping("/addOHP/{parking}/{occupation}/{idnum}")
//    public Result<?> addOHP(@RequestParam String parking,
//                            @RequestParam Integer occupation,
//                            @RequestParam String idnum) {
        public Result<?> addOHP(@PathVariable String parking,
                @PathVariable Integer occupation,
                @PathVariable String idnum) {
        /**
         * 用idnum坐绑定来源，有三种问题
         * 1、idnum不存在--不在HouseBinding中
         * 2、idnum没买房--不在HouseBinding中
         * 3、idnum已经买了房同时绑了车位--检查ParkBinding
         */

        QueryWrapper<HouseBinding> q=new QueryWrapper<>();
        q.eq("idnum",idnum);
        if (houseBindingMapper.selectOne(q)==null){
            return Result.error("-1","请检查证件号是否存在或拥有房屋");
        }

        String housenumber=houseBindingMapper.selectOne(q).getHousenumber();

        QueryWrapper<ParkBinding> q1=new QueryWrapper<>();
        q1.eq("housenumber",housenumber);
        if (parkBindingMapper.selectOne(q1)!=null){
            return Result.error("-1","该业主已经绑定过车位");
        }
        ParkBinding newBinding=new ParkBinding();
        newBinding.setParking(parking);
        newBinding.setOccupation(occupation);
        newBinding.setHousenumber(housenumber);

        //进行绑定
        parkBindingMapper.insert(newBinding);

//        更新房屋
        QueryWrapper<House> qH=new QueryWrapper<>();
        qH.eq("housenumber",housenumber);
        House house=houseMapper.selectOne(qH);
        house.setOccupation(occupation);
        house.setParking(parking);
        houseMapper.updateById(house);

        //更新车位的信息
        QueryWrapper<ParkingLot> q2 = new QueryWrapper<>();
        q2.eq("parking", newBinding.getParking());
        ParkingLot parkingLot = parkingLotMapper.selectOne(q2);
        parkingLot.setOccupation(newBinding.getOccupation());
        //然后update车位
        parkingLotMapper.updateById(parkingLot);

        System.out.println("车位绑定");
        return Result.success();
    }

    //    解除房屋与车位绑定
    @DeleteMapping("/deleteHP/{parking}")
    public Result<?> deleteOwner(@PathVariable String parking) {

//      查找
        QueryWrapper<ParkBinding> q1 = new QueryWrapper<>();
        q1.eq("parking", parking);
        ParkBinding p=parkBindingMapper.selectOne(q1);

        String housenumber=p.getHousenumber();

        QueryWrapper<House> qH=new QueryWrapper<>();
        qH.eq("housenumber",housenumber);
        House house=houseMapper.selectOne(qH);
        house.setOccupation(0);
        house.setParking(null);
        houseMapper.updateById(house);

//        删除
        parkBindingMapper.delete(q1);

        //车位设置空闲
        QueryWrapper<ParkingLot> q2 = new QueryWrapper<>();
        q2.eq("parking", parking);
        ParkingLot parkingLot = parkingLotMapper.selectOne(q2);

        parkingLot.setOccupation(0);
        parkingLotMapper.updateById(parkingLot);

        System.out.println("车位解绑");
        return Result.success();
    }
    //增加业主
//    @RequestMapping("/addOwner")
//    public Result<?> addOwner(@RequestBody ParkBinding newBinding){
//        //        先看有没有这个人
//        QueryWrapper<Owner> q=new QueryWrapper<>();
//        q.eq("idnum",newBinding.getIdnum());
//        Owner owner = ownerMapper.selectOne(q);
//        if (owner ==null){
//            return Result.error("-1","无效的证件号，请先检查输入或注册业主");
//        }
//
//        //进行绑定
//        parkBindingMapper.insert(newBinding);
//
////        更新车位的信息
//        QueryWrapper<ParkingLot> q1=new QueryWrapper<>();
//        q1.eq("parking",newBinding.getParking());
//        ParkingLot parkingLot=parkingLotMapper.selectOne(q1);
//        parkingLot.setOccupation(newBinding.getType());
//        //然后update车位
//        parkingLotMapper.updateById(parkingLot);
//
////      车位绑定数+1
//
//
//        owner.setParkProp(owner.getParkProp()+1);
//
//        //然后update业主
//        ownerMapper.updateById(owner);
//
//        return Result.success();
//    }

    //删除业主
//    @DeleteMapping("/deleteOwner/{parking}/{idnum}")
//    public Result<?> deleteOwner(@PathVariable String parking, @PathVariable String idnum){
////        查找
//        QueryWrapper<ParkBinding> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("parking", parking)
//                .eq("idnum",idnum);
//
//        // 业主车位减 1
//        QueryWrapper<Owner> q=new QueryWrapper<>();
//        q.eq("idnum",idnum);
//        Owner owner = ownerMapper.selectOne(q);
//        owner.setParkProp(owner.getParkProp()-1);
//
//        //然后update业主
//        ownerMapper.updateById(owner);
//
////        删除
//        parkBindingMapper.delete(queryWrapper);
//        System.out.println("解绑");
//
//        //车位空闲
//        QueryWrapper<ParkingLot> q2=new QueryWrapper<>();
//        q2.eq("parking",parking);
//        ParkingLot parkingLot= parkingLotMapper.selectOne(q2);
//
//        parkingLot.setOccupation(0);
//        parkingLotMapper.updateById(parkingLot);
//
//        return Result.success();
//    }
}
