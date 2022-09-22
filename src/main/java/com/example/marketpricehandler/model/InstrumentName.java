package com.example.marketpricehandler.model;

import lombok.Getter;

@Getter
public enum InstrumentName {

    EUR_USD("EUR/USD"),
    GBP_USD("GBP/USD"),
    EUR_JPY("EUR/JPY");
    private String value;

    InstrumentName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static InstrumentName fromValue(String value) {
        for (InstrumentName a : InstrumentName.values()) {
            if (a.value.equals(value)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
