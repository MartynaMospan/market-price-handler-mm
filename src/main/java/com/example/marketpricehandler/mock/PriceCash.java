package com.example.marketpricehandler.mock;

import com.example.marketpricehandler.model.InstrumentName;
import com.example.marketpricehandler.model.Price;
import org.apache.kafka.common.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceCash implements Cache<InstrumentName, Price> {

    private HashMap<InstrumentName, Price> mockedCash = new HashMap<>();

    @Override
    public Price get(InstrumentName key) {
        return mockedCash.get(key);
    }

    @Override
    public void put(InstrumentName key, Price value) {
        if(mockedCash.containsKey(key)){
            mockedCash.replace(key, value);
        }else{
            mockedCash.put(key, value);
        }
    }

    @Override
    public boolean remove(InstrumentName key) {
        Price old = mockedCash.get(key);
        return mockedCash.remove(key, old);
    }

    @Override
    public long size() {
        return mockedCash.size();
    }

    public List<Price> getAll(){
        return mockedCash.values().stream().toList();
    }
}
