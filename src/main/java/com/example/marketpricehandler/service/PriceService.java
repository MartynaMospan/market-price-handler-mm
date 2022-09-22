package com.example.marketpricehandler.service;

import com.example.marketpricehandler.AppUtils;
import com.example.marketpricehandler.exception.AppException;
import com.example.marketpricehandler.mock.InterfaceFromMQ;
import com.example.marketpricehandler.mock.PriceCash;
import com.example.marketpricehandler.model.InstrumentName;
import com.example.marketpricehandler.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class PriceService implements InterfaceFromMQ {

    private final PriceCash priceCash;

    public List<Price> getLastPrices() {
        return priceCash.getAll();
    }

    @Override
    public void onMessage(String message) {
        List<Price> prices = parseStringMessage(message).stream()
                .map(PriceMapper::applyMargins)
                .toList();
        updateCash(prices);
    }

    private void updateCash(List<Price> prices) { //assumed that in one message could appear many prices for one instrument
        for (InstrumentName instrumentName:InstrumentName.values()) {
            Price newPrice = findLastPriceForInstrument(prices, instrumentName);
            if (newPrice != null){
                priceCash.remove(instrumentName);
                priceCash.put(instrumentName, newPrice);
            }
        }
    }
    private Price findLastPriceForInstrument(List<Price> prices, InstrumentName instrumentName) {
        return prices.stream()
                .filter(price -> price.getInstrumentName() == instrumentName)
                .max(comparing(Price::getTimestamp))
                .orElse(null);
    }



    List<Price> parseStringMessage(String message){
        return message
                .lines()
                .dropWhile(String::isBlank)
                .map(this::parseLineStringToPrice)
                .toList();
    }

    Price parseLineStringToPrice(String stringLine) {
        List<String> listFromLine = Arrays.stream(stringLine
                .split(","))
                .map(String::strip)
                .dropWhile(String::isBlank)
                .toList();
        try {
            return Price.builder()
                    .id(Integer.valueOf(listFromLine.get(0)))
                    .instrumentName(InstrumentName.fromValue(listFromLine.get(1)))
                    .bid(Double.valueOf(listFromLine.get(2)))
                    .ask(Double.valueOf(listFromLine.get(3)))
                    .timestamp(LocalDateTime.parse(listFromLine.get(4), AppUtils.formatter))
                    .build();
        }catch (ParseException e){
            throw new AppException("Bad format of the line in the message");
        }catch (ArrayIndexOutOfBoundsException e){
            throw new AppException("Missing data in the line of the message");
        }
    }
}
