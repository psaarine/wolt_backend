package com.woltappsummer.BackendTest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
		"my-properties.filename=testifile"
})
class BackendTestApplicationTests {


	@Test
	void contextLoads() {

	}


}
