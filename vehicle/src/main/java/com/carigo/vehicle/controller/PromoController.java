package com.carigo.vehicle.controller;

import com.carigo.vehicle.model.Promo;
import com.carigo.vehicle.service.PromoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promos")
@RequiredArgsConstructor
public class PromoController {

    private final PromoService promoService;

    // CREATE
    @PostMapping
    public ResponseEntity<Promo> create(@RequestBody Promo promo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(promoService.createPromo(promo));
    }

    // LIST
    @GetMapping
    public ResponseEntity<List<Promo>> list() {
        return ResponseEntity.ok(promoService.getAllPromos());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Promo> update(
            @PathVariable Long id,
            @RequestBody Promo promo) {

        return ResponseEntity.ok(promoService.updatePromo(id, promo));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        promoService.deletePromo(id);
        return ResponseEntity.ok("Promo deleted successfully");
    }
}
