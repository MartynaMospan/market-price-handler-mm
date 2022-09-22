package com.example.marketpricehandler.service;

import com.example.marketpricehandler.AppUtils;
import com.example.marketpricehandler.model.Price;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

    public static Price applyMargins(Price price){
        Double oldBid = price.getBid();
        Double newBid = oldBid - (oldBid * AppUtils.BID_MARGIN);
        Double oldAsk = price.getAsk();
        Double newAsk = oldAsk + (oldAsk * AppUtils.ASK_MARGIN);
        price.applyMargins(DoubleRounder.round(newBid, 5), DoubleRounder.round(newAsk, 5));
        return price;
    }

    public static String prettyPrinting(Price price) {
        String formattedTimeStamp = price.getTimestamp().format(AppUtils.formatter);
        return "Price(id=" + price.getId()
                + ", instrumentName=" + price.getInstrumentName().toString()
                + ", bid=" + price.getBid()
                + ", ask=" + price.getAsk()
                + ", timestamp=" + formattedTimeStamp + ")";
    }

}
