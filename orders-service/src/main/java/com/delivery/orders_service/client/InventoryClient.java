package com.delivery.orders_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 🎯 Se conecta al inventario usando el nombre que registraste en su YML
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    // ⚡ Apuntamos exactamente al método que descuenta stock en tu DishController del inventario
    @PutMapping("/api/dishes/descontar-stock")
    void descontarStock(@RequestParam("id") Long id, @RequestParam("cantidad") Integer cantidad);
}
