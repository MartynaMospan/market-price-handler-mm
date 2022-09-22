package com.example.marketpricehandler.service;

import com.example.marketpricehandler.AppUtils;
import com.example.marketpricehandler.exception.AppException;
import com.example.marketpricehandler.model.InstrumentName;
import com.example.marketpricehandler.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceServiceTest {

    @Autowired
    private PriceService priceService;

    @Test
//    @DisplayName("parseStringMessage - SingleLine - OK")
    void parseStringMessage_ExtraEmptyLine_shouldReturnPriceDtoList(){
        //given
        String message = "\n106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001";

        //when
        List<Price> priceDtoList = priceService.parseStringMessage(message);
        Price priceDto = priceDtoList.get(0);

        //then
        assertAll("retrieved price DTO",
                () -> assertEquals(106, priceDto.getId()),
                () -> assertEquals(InstrumentName.fromValue("EUR/USD"), priceDto.getInstrumentName()),
                () -> assertEquals(Double.valueOf("1.1000"), priceDto.getBid()),
                () -> assertEquals(Double.valueOf("1.2000"), priceDto.getAsk()),
                () -> assertEquals(LocalDate.parse("01-06-2020 12:01:01:001", AppUtils.formatter), priceDto.getTimestamp())
        );
    }

    @Test
    void parseLineStringToPriceDto_MissingData_shouldThrowAppException(){
        //given
        String lineFromMessage = "106, EUR/USD, 1.1000,1.2000";

        //when & then
        assertThrows(AppException.class, () -> {
            priceService.parseLineStringToPrice(lineFromMessage);
        });
    }

    @Test
//    @DisplayName("parseStringMessage - SingleLine - OK")
    void parseLineStringToPriceDto_CorrectMessageLine_shouldReturnPriceDto(){
        //given
        String lineFromMessage = "106, EUR/USD, 1.1000,1.2000, 01-06-2020 12:01:01:001";

        //when
        Price priceDto = priceService.parseLineStringToPrice(lineFromMessage);

        //then
        assertAll("retrieved price DTO",
                () -> assertEquals(106, priceDto.getId()),
                () -> assertEquals(InstrumentName.fromValue("EUR/USD"), priceDto.getInstrumentName()),
                () -> assertEquals(Double.valueOf("1.1000"), priceDto.getBid()),
                () -> assertEquals(Double.valueOf("1.2000"), priceDto.getAsk()),
                () -> assertEquals(LocalDate.parse("01-06-2020 12:01:01:001", AppUtils.formatter), priceDto.getTimestamp())
        );
    }

    @Test
    void onMessage_CorrectMessage_shouldSavePricesToCashAndPrintThem(){
        //given
        String message = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002\n" +
                "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
                "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110\n";
        Price price_EUR_JPY = new Price(110, InstrumentName.EUR_JPY, 107.649, 131.901, LocalDateTime.parse("01-06-2020 12:01:02:110", AppUtils.formatter));
        Price price_GBP_USD = new Price(109, InstrumentName.GBP_USD, 1.12041, 1.38171, LocalDateTime.parse("01-06-2020 12:01:02:100", AppUtils.formatter));
        Price price_EUR_USD = new Price(106, InstrumentName.EUR_USD, 0.99, 1.32, LocalDateTime.parse("01-06-2020 12:01:01:001", AppUtils.formatter));
        List<Price> expectedResponse = List.of(price_EUR_JPY, price_GBP_USD, price_EUR_USD);

        //when
        priceService.onMessage(message);
        List<Price> prices = priceService.getLastPrices();
        Price actual_EUR_USD = prices.stream().filter(it -> it.getInstrumentName() == InstrumentName.EUR_USD).findFirst().orElseThrow();

        //then
        assertAll("retrieved prices",
                () -> assertEquals(actual_EUR_USD.getId(), price_EUR_USD.getId()),
                () -> assertEquals(actual_EUR_USD.getBid(), price_EUR_USD.getBid()),
                () -> assertEquals(actual_EUR_USD.getAsk(), price_EUR_USD.getAsk()),
                () -> assertEquals(actual_EUR_USD.getTimestamp(), price_EUR_USD.getTimestamp())
        );
        prices.forEach(PriceMapper::prettyPrinting);
    }

}