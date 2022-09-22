---
title: DBMS课设 --物业信息管理
categories: 
- 数据库
typora-root-url: ..\..

---

Property Information Management（prop_info）

作业时间2022.8.14-2022.9.1，大概17天

<!--more-->

## DBMS课设 --物业信息管理

Property Information Management（prop_info）

作业时间2022.8.14-2022.9.1，大概17天



主要信息管理: 业主信息、房屋信息

■费用收取： 物业费、水电煤气、有线电视、供暖、车位

■信息查询及报表:  月报表、季报表、年报表



## 项目结构

前端：vue2, element.ui

后端：SpringBoot, MybatisPlus

数据库：Mysql

版本信息在具体模块查看

## 系统功能分析

定位为供物业工作人员使用的信息管理系统，功能分为四大模块

![功能模块](/DBMS/prop_info/readme/功能模块.png)



具体功能通过用例图来展示

![用例图](/DBMS/prop_info/readme/用例图.png)



### 部分功能分析

#### 数据分析

初期：房屋、业主、车位设置（3000-10000）

稳定后：缴费（月增2000-4000）

#### 限购

房屋、车位限购

房屋：业主=1：n

房屋：车位=1：1

#### 总账单

累加式(级联思想)

个人缴费———个人账单更新———物业总账单更新

#### 其他安全机制

- 绑定状态下不可更新或删除（房屋、车位、业主个人信息）。
- 房屋、车位、业主信息的冲突检查。
- 暂无房产业主不可缴费，不可购入或租入车位。
- 仅租车位业主应缴车位费。
- 共同业主共享缴费清单，避免重复缴费。

### 房屋管理

房屋信息

![房屋信息](/DBMS/prop_info/readme/房屋信息.png)

房屋-查看业主

![房屋-查看业主](/DBMS/prop_info/readme/房屋-查看业主.png)

房屋编辑&新增

![房屋编辑&新增](/DBMS/prop_info/readme/房屋编辑&新增.png)

### 车位管理

车位信息

![车位信息](/DBMS/prop_info/readme/车位信息.png)

车位-查看业主

![车位-查看信息](/DBMS/prop_info/readme/车位-查看信息.png)



车位-编辑&新增

![车位编辑&新增](/DBMS/prop_info/readme/车位编辑&新增.png)





### 业主管理

业主信息

![业主信息](/DBMS/prop_info/readme/业主信息.png)

业主-房产车位

![业主-房产车位](/DBMS/prop_info/readme/业主-房产车位.png)

业主-缴费![业主-缴费](/DBMS/prop_info/readme/业主-缴费.png)

业主-房产车位

![业主-房产车位](/DBMS/prop_info/readme/业主-房产车位.png)

### 物业账单

物业账单![物业账单](/DBMS/prop_info/readme/物业账单.png)

物业账单-年与季筛选

![物业账单-年与季筛选](/DBMS/prop_info/readme/物业账单-年与季筛选.png)



物业账单-月筛选![物业账单-月筛选](/DBMS/prop_info/readme/物业账单-月筛选.png)



报表-折线

![报表-折线](/DBMS/prop_info/readme/报表-折线.png)

报表-饼

![报表-饼](/DBMS/prop_info/readme/echart-pie.png)



## 前端prop_vue

### 项目搭建

1、环境配置：

node  v16.13.1

npm   v8.14.0

vue     @vue/cli 5.0.8

2、使用@vue/cli脚手架构建一个vue2项目

地址：D:\code\DBMS\prop_info\prop_vue

```
1）将vue项目建在D盘的目标文件夹下，cmd中进入该目录，输入新建项目命令 vue create 项目名，执行后会自动生成
vue项目。
2）运行vue项目
3）浏览器访问项目
项目运行成功后浏览器访问地址 http://localhost:8080
4）查看生成的vue项目结构

> vue create prop_info(名字不能含有大写字母npm)
> npm run serve

安装的cli插件：router, vuex, bable

5）使用IntelliJ IDEA打开并运行该vue项目
点击菜单栏“Add Configurations”设置运行命令npm
点击“serve”旁边运行按钮
```

3. 引入element.ui（在main.js中引入）

[安装 | Element Plus (gitee.io)](https://element-plus.gitee.io/zh-CN/guide/installation.html#使用包管理器)

element.ui只适配Vue2，支持Vue3的是Element.plus



4. 跨域设置

	前后端应该占用本地不同的端口，同一端口会冲突。

	通过这个文件，实现前后端的连接。

	位置在vue文件夹下，和package.json、src文件夹平级

	文件名 vue.config.js

	```
	// 跨域配置
	module.exports = {
	    devServer: {                //记住，别写错了devServer//设置本地默认端口  选填
	        // port: 8081,
	        proxy: {                 //设置代理，必须填
	            '/api': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
	                target: 'http://localhost:8081',     //代理的目标地址
	                changeOrigin: true,              //是否设置同源，输入是的
	                pathRewrite: {                   //路径重写
	                    '/api': ''                     //选择忽略拦截器里面的单词
	                }
	            }
	        }
	    }
	}
	```

5. 新建一个页面

	需要在router的index.js中添加路由，在导航中设置路径

	```
	{
	    path: '/house',
	    name: 'House',
	    component: House
	}
	      <el-menu-item index="/house" style="font-size: 20px "><i class="el-icon-s-home"></i> 房屋管理</el-menu-item>
	```

### 功能

前端东西很多且复杂，讲不清楚。有时间还是自己学学，参考自己做的项目。

此次的数据库课设，让我基本能独立搭建好一个管理系统的前端页面，不过vue的很多属性我确实也不会用。但就我用到的东西而言，已经能自己找出bug并修复了。

组件很重要，但是不太熟练。



奥，比较重要的一点是学会使用已有的前端开源框架，比如若依，这样省了很多事。就算不用，参考一些功能设计和ui也会有很大帮助。虽然这个是我做完项目之后，跟人交流时才知道的。

[RuoYi 若依官方网站 ](http://www.twom.top/)

### 问题

> 如何在页头的content中自定义变量

```
开始用的tag
<el-page-header @back="back" content="房屋&业主绑定" style="color: blue"></el-page-header>
<el-tag size="medium">房屋编号:{{ this.housenumber }}</el-tag>

后来
<el-page-header @back="back" content={{this.headercontent}} style="color: blue"></el-page-header>

报错，提示
修改content={{this.headercontent}}
为:content=this.headercontent
```

> 如何引入echarts？

[如何在Vue中使用Echarts可视化库_](https://blog.csdn.net/qq_43471802/article/details/109136061?utm_medium=distribute.pc_category.none-task-blog-hot-2.nonecase&depth_1-utm_source=distribute.pc_category.none-task-blog-hot-2.nonecase&request_id=)

## 后端prop_serve

SpringBoot+MybatisPlus

版本信息：

```
SpringBoot  2.7.2
MybatisPlus 3.5.2
jdk 1.8
```

### 项目搭建

地址D:\code\DBMS\prop_info\prop_serve

(若使用idea操作，在大文件夹下D:\code\DBMS\prop_info创建一个子模块maven即可)

方式一：直接使用idea创建（似乎因为网络问题，我失败了几次）

1. [使用SpringBoot搭建Web项目](https://www.cnblogs.com/ljsh/p/14279992.html)

2. 创建spring_boot项目时，出现Error message: Cannot download ‘https://start.spring.io‘: Read timed out，即连不上网络访问https://start.spring.io，怀疑是家里移动网的问题，按[方法](https://blog.csdn.net/weixin_45721753/article/details/125933528)更换其为阿里云的地址即可https://start.aliyun.com

3. 添加依赖，位置pom.xml

4. springweb, Lombok, MyBatis plus

	

  方式二：通过[官网]([Spring Initializr](https://start.spring.io/))来创建，类似vue

  （网络问题再现，移动网mmp，挂了梯子才行。

Could not connnect to server. Please check your network.



### 功能

#### 项目细节初见

初见

先准备好几个文件夹，用于放不同类型的java文件

如entity，存放项目的实体类；controller存放业务功能类；mapper存放mapper文件。

##### entity类

entity类注解，以Owner类为例。了解更多搜索“Mybatis-Plus实体类注解”，如[Mybatisplus常用注解_袁浩东的博客-CSDN博客_mybatisplus注解](https://blog.csdn.net/qq_42758288/article/details/113888770)

```
@Data//使用lombok插件，省略了getter和setter的书写
@TableName("owner")//表名与数据库中的表对应一致
public class Owner {
	//主键的注解
    @TableId(value="seleId",type = IdType.AUTO)
    private Long seleId;//作为选择数据的标记
	
	//其他字段的注解，当二者不一致时必须要用，比如数据库中字段是id_num，注解就写@TableField("id_num")。一致时可以省略。
 	@TableField("idnum")
    private String idnum;//身份证
    private String name;//姓名
    private int gender;//性别
    private String phone;//联系方式
    private String work;//工作单位
    private int family;//家庭人数
//    不要了
//    @TableField("houseProp")
//    private int houseProp;//名下房产数
//    @TableField("parkProp")
//    private int parkProp;//名下私有车位数
}
```

##### mapper类

格式如下，其中@Mapper注解要看情况，有两种方式。

一、每个mapper类前都加@Mapper注解；

```
文件名  OwnerMapper.interface
@Mapper
public interface OwnerMapper extends BaseMapper<Owner> {
}
```

二、在项目启动类xxxApplication.java 前写@MapperScan("com.prop_serve.mapper")

```
@SpringBootApplication//启动类的必要注解
@MapperScan("com.prop_serve.mapper")
public class PropServeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropServeApplication.class, args);
	}

}
```

但其实使用第二种的MapperScan是最省力的，因为如果不在这里写，每个mapper都要自己手动注释@Mapper。

**注意：二者都存在的话，就会产生重复注入警告**



##### controller类

```
@RestController  //必要的注解
@RequestMapping("/owner")//前端访问路径
public class OwnerControl {
	@Resource  //mapper引导，每用一个不同的mapper，就加一个注解。
    OwnerMapper ownerMapper;
    
	@RequestMapping("/owner")//进一步细分路径
	public void Test(){
	}
}
```

##### common类

存放其他的一些配置文件类，比如result.java（对返回结果进行包装）和MybatisPlusConfig.java  分页插件

#### 数据库连接

在java/resource 中新建文件 application.yml，用于设置端口和连接数据库

```
#  数据库连接配置文件
server:
  port: 8081

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/prop_info?useUnicode=true&characterEncoding=UTF-8&useSSL=false &serverTimezone=Asia/Shanghai #注意时区设置
    username: root
    password: root

mybatis-plus:
  check-config-location: true    #
  mapper-locations: classpath:mapper/*.xml #mapper位置
  configuration:
    map-underscore-to-camel-case: true #开启驼峰命名
    type-aliases-package: com.prop_serve.entity #实体类位置
```



#### 增加功能

```
@RequestBody 参数注解，对应的参数需要是一个实体类，通过mapper的insert方法插入数据

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
```

#### 编辑功能

```
 通过mapper的updateById方法更新数据，前提是对象中含有主键。
 如果没有，就先通过参数的一些属性，尝试从数据库中找到符合特点的数据（selectOne, selectList,selectPage等），然后进行更新。
 
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
```



#### 删除功能

```
1.通过querry 找到对应数据，使用delete()方法：ownerMapper.delete(q2)，使用forEach遍历批量删除数据;
2.传入的是个对象，直接用deleteById()，批量删除为deleteBatchIds()


//    单个删除用户
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
```

#### 查询功能

```
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
```

#### 分页

说不太清楚，看文件吧

此处是LIst转page的方法

```
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
```

### 问题

> 项目一直卡在resolving dependencies of xxx, 或者说resolving Maven dependencies

解决方式参考

[IDEA新建项目卡在下载Resolving dependencies of xxx](https://blog.csdn.net/sun_luming/article/details/95327506)



> Cannot resolve symbol ‘SpringBootApplication

很可能是因为没连数据库但是使用了mybatis-plus依赖的缘故，总之先注释掉m-p就行了。

其他情况，Maven设置中左上角刷新和install

以及

[IDEA “Cannot resolve symbol” 解决办法_悟能的师兄的博客-CSDN博客_cannot resolve symbol](https://blog.csdn.net/yexiaomodemo/article/details/119238320)



> 后端接口的验证方法

学习使用apipost工具

>  批量删除功能无效

后端参数RequestBody注解，但误用了DeleteMapping而不是RequestMapping

## 数据库prop_info

Server version: 8.0.27 MySQL Community Server - GPL



使用方式：以管理员身份运行cmd窗口，输入net start mysql 或net stop mysql来启动mysql

mysql连接方式 

```
MySQL -uxxx -pyyy  #xxx和yyyy分别是用户名和密码
```



### 数据库设计

**ER图和表结构**

说明：表中设置seleId选择标记的作用是，实现对unique属性的编辑。比如说要修改一个人的证件号和其他身份信息，如果把证件号作为主键，后端实现修改功能时就会很复杂，而且这样的操作也是危险的，因为它是与其他信息有所关联的。

#### ER图

![物业管理系统ER图全属性](/DBMS/prop_info/readme/物业管理系统ER图全属性.png)

#### 业主表owner

| Field  | 约束                     | Type                               | Comment            |
| ------ | ------------------------ | ---------------------------------- | ------------------ |
| seleId | 主键，unique，自增，非空 | int(10) unsigned zerofill NOT NULL | 作为选中数据的标记 |
| name   | 非空                     | varchar(50) NOT NULL               | 业主姓名           |
| gender | 非空                     | int NOT NULL                       | 业主性别           |
| idnum  | 非空，unique             | varchar(50) NOT NULL               | 证件号             |
| phone  | 非空                     | varchar(50) NOT NULL               | 联系方式           |
| work   | 非空                     | varchar(100) NOT NULL              | 工作单位           |
| family | 默认“1”                  | int NULL                           | 家庭人数           |

说明：表中设置seleId选择标记的作用是，实现对unique属性的编辑和校验。

比如说要修改一个人的证件号和其他身份信息，如果把证件号作为主键，后端实现修改功能时就会很复杂，而且这样的操作也是危险的，因为它是与其他信息有所关联的。

#### 房屋表house

| Field       | 约束                     | Type                 | Comment                        |
| ----------- | ------------------------ | -------------------- | ------------------------------ |
| seleId      | 主键，unique，自增，非空 | int NOT NULL         | 标记，作为选中数据的标记，自增 |
| housenumber | unique，非空             | varchar(10) NOT NULL | 房屋编号，格式为_b_u_f_d       |
| housetype   | 非空                     | varchar(10) NOT NULL | 户型，格式为_r_h_t             |
| area        | 非空，默认40（最小40）   | float NOT NULL       | 面积,单位平米                  |
| hnum        | 默认0                    | int NULL             | 业主人数，默认为0              |
| h_building  |                          | varchar(10) NULL     | 栋                             |
| h_unit      |                          | varchar(10) NULL     | 单元                           |
| h_floor     |                          | varchar(10) NULL     | 层                             |
| h_door      |                          | varchar(10) NULL     | 门牌（户）                     |
| parking     | 默认“无”                 | varchar(20) NULL     | 绑定车位                       |
| occupation  | 默认“0”                  | int NULL             | 车位状态                       |



#### 房产绑定表 housebinding

| Field       | 约束         | Type                 | Comment                                              |
| ----------- | ------------ | -------------------- | ---------------------------------------------------- |
| idnum       | 主键，unique | varchar(50) NOT NULL | 业主证件号，每个人限购一套房，所以主键为证件号更方便 |
| housenumber |              | varchar(50) NOT NULL | 房屋编号，同一套房可以有多个共同业主共享产权         |



#### 车位表 parkinglot

| Field      | 约束                     | Type                          | Comment                                      |
| ---------- | ------------------------ | ----------------------------- | -------------------------------------------- |
| seleId     | 主键，unique，自增，非空 | int NOT NULL                  | 作为选中数据的标记                           |
| parking    | unique，非空             | varchar(20) NOT NULL          | 停车位编号                                   |
| occupation | 非空，默认“0”            | int(1) unsigned zerofill NULL | 转让情况， 0表可租或可售，1表已租，2表已售出 |



#### 车位绑定parkbinding

| Field       |                    | 约束Type             | Comment                                            |
| ----------- | ------------------ | -------------------- | -------------------------------------------------- |
| parking     | 主键，unique，非空 | varchar(20) NOT NULL | 停车位                                             |
| housenumber | 非空               | varchar(10) NOT NULL | 房屋号，小区业主才能购买或者租入车位，一户最多一个 |
| occupation  | 非空               | int NOT NULL         | 所属情况（2表购入，1表租入）                       |

一房一车，但是绑定时输入的是业主的证件号，填入数据库的是查询到的绑定的房屋编号



#### 房屋缴费表perfee

| Field       | 约束               | Type                 | Comment            |
| ----------- | ------------------ | -------------------- | ------------------ |
| seleId      | 主键，unique，自增 | int NOT NULL         | 作为选中数据的标记 |
| housenumber |                    | varchar(10) NOT NULL | 房屋编号           |
| chargetime  |                    | date NULL            | 收款月份           |
| expectfee   | 默认0              | double NULL          | 预计总费           |
| property    | 默认0              | double NULL          | 物业费             |
| water       | 默认0              | double NULL          | 水                 |
| elec        | 默认0              | double NULL          | 电                 |
| gas         | 默认0              | double NULL          | 天然气             |
| tv          | 默认0              | double NULL          | 电视               |
| heating     | 默认0              | double NULL          | 暖气               |
| park        | 默认0              | double NULL          | 车位费（租）       |



#### 物业总缴费表

| Field      | 约束               | Type         | Comment                    |
| ---------- | ------------------ | ------------ | -------------------------- |
| seleId     | 主键，unique，自增 | int NOT NULL | 作为选中数据的标记         |
| chargetime | unique             | date NULL    | 收款月份，设为独特防止冲突 |
| totalf     | 默认0              | double NULL  | 预计总费                   |
| property   | 默认0              | double NULL  | 物业费                     |
| water      | 默认0              | double NULL  | 水                         |
| elec       | 默认0              | double NULL  | 电                         |
| gas        | 默认0              | double NULL  | 天然气                     |
| tv         | 默认0              | double NULL  | 电视                       |
| heating    | 默认0              | double NULL  | 暖气                       |
| park       | 默认0              | double NULL  | 车位                       |





### 模拟数据批量生成

一、使用网站或其他工具

[批量生成模拟数据在线工具—工具猫 (toolscat.com)](https://www.toolscat.com/dev/data-generator)

二、结合网站工具和excel、txt模拟复杂格式的数据

[使用Excel批量生成sql,包括日期格式_我的屎壳郎君的博客-CSDN博客](https://blog.csdn.net/qq_27468351/article/details/79728233)

如时间，特殊格式的字符串，求和，字符串合并

excel很强大，数据可以按格式自动赋值传递。

时间类的字段直接用是数字类型的，如果想保留为字符串，就先复制到txt文件中，再设置目标行的段落格式为文本类型，然后粘贴即可。

总之，模拟好数据后，总要转化成sql脚本。

方法为

```
=CONCATENATE("insert ignore into TableName (属性1,属性2,属性3) value ('"&B2&"',"&C2&","&D2&");")

解析：
1、 =CONCATENATE("sql-script") sql-script指的是sql脚本
2、 insert ignore into TableName的ignore指的是忽略插入错误的行，执行所有没有问题的数据，这在批量执行时有用。
3、 "&C2&" 双引号中包裹的是excel表格中目标数据位置，如果是字符串，还要套上一层单引号'"&B2&"'
```

