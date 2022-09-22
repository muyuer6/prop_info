package com.prop_serve;

import com.prop_serve.common.Result;
import com.prop_serve.mapper.HouseMapper;
import com.prop_serve.entity.House;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PropServeApplicationTests {


	@Autowired
	private HouseMapper houseMapper;

	@Test
	public void testSelect() {
		System.out.println(("----- selectAll method test ------"));
		List<House> houseList = houseMapper.selectList(null);
		Assert.assertEquals(3, houseList.size());
		houseList.forEach(System.out::println);
	}


}
