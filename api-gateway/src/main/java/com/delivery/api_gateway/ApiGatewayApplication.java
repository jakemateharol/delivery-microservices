package com.delivery.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}



	@RestController
class FallbackController {

    @GetMapping("/fallback/orders")
    public String ordersFallback() {
        return "El sistema de órdenes del restaurante no se encuentra disponible en este momento. " +
               "Por favor, intente realizar su pedido más tarde. (Protección Circuit Breaker Activa)";
    }
}

}


