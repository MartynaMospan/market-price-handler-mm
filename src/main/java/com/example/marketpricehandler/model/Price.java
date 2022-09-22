package com.example.marketpricehandler.model;


import com.example.marketpricehandler.AppUtils;
import lombok.*;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Price {

    @Id
    private Integer id;
    private InstrumentName instrumentName;
    private Double bid;
    private Double ask;
    private LocalDateTime timestamp;

    public void applyMargins(Double bidMargin, Double askMargin){
        bid = bidMargin;
        ask = askMargin;
    }
}
