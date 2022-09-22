package com.example.marketpricehandler;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
@Getter
public final class AppUtils {

    public static final  Double BID_MARGIN = 0.1;
    public static final Double ASK_MARGIN = 0.1;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
}
