package org.yvesguilherme.statistics.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.statistics.commons.FileUtils;
import org.yvesguilherme.statistics.service.TransactionService;

import java.util.stream.Stream;

@WebMvcTest(controllers = StatisticsController.class)
@ComponentScan(basePackages = "org.yvesguilherme.statistics")
class TransactionControllerTest {
  private static final String URL = "/transacao";

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TransactionService service;

  @Test
  @DisplayName("POST /transacao creates a transaction and returns 200 with empty body")
  void post_CreatesATransaction_AndReturns200_WithEmptyBody() throws Exception {

    var request = fileUtils.readResourceFile("transacao/post-request-201.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(""));
  }

  @ParameterizedTest
  @DisplayName("POST /transacao returns UnProcessableEntity HttpStatus 422 when transaction is invalid")
  @MethodSource("provideInvalidTransactionRequests")
  void post_ReturnsUnprocessableEntity_WhenTransactionIsInvalid(String request) throws Exception {

    mockMvc.perform(
                    MockMvcRequestBuilders.post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andExpect(MockMvcResultMatchers.content().string(""));
  }

  @ParameterizedTest
  @DisplayName("POST /transacao returns HttpStatus 400 when request is invalid")
  @MethodSource("provideInvalidTransactionJsonRequests")
  void post_ReturnsBadRequest_WhenRequestIsInvalid(String request) throws Exception {

    mockMvc.perform(
                    MockMvcRequestBuilders.post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(""));
  }

  private static Stream<Arguments> provideInvalidTransactionRequests() {
    return Stream.of(
            Arguments.of("{\"valor\":123.45,\"dataHora\":\"" + java.time.OffsetDateTime.now().plusMinutes(2) + "\"}"),
            Arguments.of("{\"valor\":0.0,\"dataHora\":\"" + java.time.OffsetDateTime.now() + "\"}"),
            Arguments.of("{\"valor\":-1,\"dataHora\":\"" + java.time.OffsetDateTime.now() + "\"}")
    );
  }

  private static Stream<Arguments> provideInvalidTransactionJsonRequests() {
    return Stream.of(
            Arguments.of(""),
            Arguments.of("{\"valor\":-1,\"dataHora\":\"\"}"),
            Arguments.of("{\"valor\":\"invalid\",\"dataHora\":\"invalid-date\"}")
    );
  }

  @Test
  @DisplayName("DELETE /transacao returns 200 with empty body")
  void delete_Returns200_WithEmptyBody() throws Exception {
    mockMvc.perform(
                    MockMvcRequestBuilders.delete(URL)
                            .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""));
  }
}