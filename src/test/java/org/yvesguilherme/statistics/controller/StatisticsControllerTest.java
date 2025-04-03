package org.yvesguilherme.statistics.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.statistics.commons.FileUtils;
import org.yvesguilherme.statistics.domain.Statistics;
import org.yvesguilherme.statistics.service.TransactionService;

@WebMvcTest(controllers = StatisticsController.class)
@ComponentScan(basePackages = "org.yvesguilherme.statistics")
class StatisticsControllerTest {
  private static final String URL = "/estatistica";

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TransactionService service;

  @Test
  @DisplayName("GET /estatistica returns 200 with statistics")
  void post_ReturnsHttp200_WithStatistics() throws Exception {
    BDDMockito.when(service.getStatistics()).thenReturn(new Statistics(0, 0.0, 0.0, 0.0, 0.0));

    var expectedResponse = fileUtils.readResourceFile("estatistica/get-response-empty-queue-200.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.get(URL)
                            .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
  }

  @Test
  @DisplayName("GET /estatistica returns 200 with full queue statistics")
  void get_ReturnsHttp200_WithFullQueueStatistics() throws Exception {
    var expectedStatistics = new Statistics(2, 200.0, 100.0, 100.0, 100.0);
    BDDMockito.when(service.getStatistics()).thenReturn(expectedStatistics);

    var expectedResponse = fileUtils.readResourceFile("estatistica/get-response-full-queue-200.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.get(URL)
                            .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
  }

}