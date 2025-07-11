package com.coffeenote.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
class CoffeenoteApiApplicationTests {

	@Test
	void contextLoads() {
		// 測試 Spring 應用程式上下文是否能正常載入
		// 排除 Security 自動配置以避免循環依賴問題
	}

}
