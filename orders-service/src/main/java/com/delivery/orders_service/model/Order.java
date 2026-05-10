package com.delivery.orders_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El cliente no puede estar vacío")
    private String customerName;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @NotBlank(message = "El producto no puede estar vacío")
    private String product;

    @NotNull
    @Positive(message = "El total debe ser mayor a 0")
    private Double total;

    private String status;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}