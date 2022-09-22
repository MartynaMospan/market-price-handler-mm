package com.example.marketpricehandler.controller;

import com.example.marketpricehandler.model.Price;
import com.example.marketpricehandler.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/prices")
    public ResponseEntity<List<Price>> getLastPrices() {
        return ResponseEntity.ok(priceService.getLastPrices());
    }
}
