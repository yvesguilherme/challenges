package org.yvesguilherme.statistics.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("GET /actuator/health returns 200")
  void get_ActuatorHealth_ReturnsHttp200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
            .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("GET /actuator/info returns 200")
  void get_ActuatorInfo_ReturnsHttp200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/actuator/info"))
            .andExpect(MockMvcResultMatchers.status().isOk());
  }
}