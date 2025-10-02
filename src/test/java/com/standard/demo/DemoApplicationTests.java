package com.standard.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	  @Autowired
    private MockMvc mockMvc;
	@Test
	void contextLoads() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(MockMvcResultMatchers.content().string("Hola Spring"));
		
	}

}
