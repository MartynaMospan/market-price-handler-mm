package com.example.marketpricehandler.controller;

import com.example.marketpricehandler.mock.PriceCash;
import com.example.marketpricehandler.service.PriceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PriceController priceController;

    @Autowired
    private PriceService priceService;

    @Autowired
    private PriceCash priceCash;

    @Test
    @DisplayName("GET - /prices - Ok")
    void findAllTimeEntries_CashNotEmpty_ShouldReturnOk() throws Exception {

        //given
        String message = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002\n" +
                "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
                "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110\n";

        //when
        priceService.onMessage(message);

        //then
        mockMvc.perform(get("/prices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize((int) priceCash.size())))
                .andDo(print());
    }

}