package com.prop_serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.prop_serve.common.Result;
import com.prop_serve.entity.House;
import com.prop_serve.entity.PerFee;
import com.prop_serve.entity.TotalFee;
import com.prop_serve.mapper.PerFeeMapper;
import com.prop_serve.mapper.TotalFeeMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

import java.util.*;

@RestController
@RequestMapping("/bill")
public class TotalFeeControl {
    @Resource
    TotalFeeMapper totalFeeMapper;
    @Resource
    PerFeeMapper perFeeMapper;

    @RequestMapping
    public Result<?> getBill() {
//        设为倒序
        QueryWrapper q=new QueryWrapper<>().orderByDesc("chargetime");
        return Result.success(totalFeeMapper.selectPage(new Page<>(1,10000),q));
    }


    public List<TotalFee> selectByYear(Date year, int season) {


//          使用calendar类方法,设置年份上限
        Calendar upYear = Calendar.getInstance();

        Calendar downYear = Calendar.getInstance();

        //选择特定季度
        if (season != 0) {
            int addDay=24*60*60*1000;//接收到的时间比传进的少24h,就加回去
            year.setTime(year.getTime()+addDay);
            upYear.setTime(year);
            downYear.setTime(year);
            System.out.println("第"+(season+2)/3+"季度");

            //设置区间下限
            downYear.set(Calendar.MONTH, season-2);
            //设置区间上限
            upYear.set(Calendar.MONTH, season+2);

        } else {
            upYear.setTime(year);
            downYear.setTime(year);
            //season=0,全选，加一年
            System.out.println("全年");
            upYear.set(Calendar.YEAR, upYear.get(Calendar.YEAR) + 1);

        }
        System.out.println("down:"+downYear.getTime());
        System.out.println("up:"+upYear.getTime());

//        time为目标年份,up为目标年+1,用于筛选
//        先找出全部数据
        QueryWrapper<TotalFee> q=new QueryWrapper<>();
        List<TotalFee> totalFees = totalFeeMapper.selectList(q.orderByDesc("chargetime"));
        List<TotalFee> result = new ArrayList<>();

        Date down=downYear.getTime();
        Date up=upYear.getTime();
//        再一一比较筛选
        totalFees.forEach(item -> {
            if (down.before(item.getChargetime()) && up.after(item.getChargetime())) {
                result.add(item);
            }
        });

        return result;
    }


    @GetMapping
    public Result<?> getPageBill(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "7") Integer pageSize,
                                 @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date year,
                                 @RequestParam(defaultValue = "") Integer season,
                                 @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date month) throws ParseException {

        QueryWrapper<TotalFee> q = new QueryWrapper<>();
        //        输入了年份,可能有季度
        if (year != null) {
            System.out.println("按年与季度筛选");
            List<TotalFee> list = selectByYear(year, season);

            return Result.success(new Page<TotalFee>().setRecords(list));
//            return Result.success(getPages(pageNum, pageSize, list));

        } else if (month != null) {
//            month不为空，先加一天，然后直接equal
            int addDay = 24 * 60 * 60 * 1000;//接收到的时间比传进的少24h,就加回去
            month.setTime(month.getTime() + addDay);
            System.out.println("month:" + month);
            q.eq("chargetime", month);
            System.out.println("按月筛选");
        } else {
            System.out.println("全部数据");
        }
        //按时间升序排列
        Page<TotalFee> page = totalFeeMapper.selectPage(new Page<>(pageNum, pageSize), q.orderByDesc("chargetime"));
//        System.out.println("pageNum:"+page.getCurrent());
//        System.out.println("pageSize:"+page.getSize());
//        System.out.println("Records:"+page.getRecords());
//        System.out.println("Total:"+page.getTotal());
//        System.out.println("Pages:"+page.getPages());
        return Result.success(page);
    }

    /**
     * 分页函数
     *
     * @param currentPage 当前页数
     * @param pageSize    每一页的数据条数
     * @param list        要进行分页的数据列表
     * @return 当前页要展示的数据
     * @author pochettino
     */
    private Page getPages(Integer currentPage, Integer pageSize, List list) {
        Page page = new Page();
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (pageSize > size) {
            pageSize = size;
        }
        if (pageSize != 0) {
            // 求出最大页数，防止currentPage越界
            int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
            if (currentPage > maxPage) {
                currentPage = maxPage;
            }
        }
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;

        List pageList = new ArrayList();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }

        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);

        return page;
    }
}

