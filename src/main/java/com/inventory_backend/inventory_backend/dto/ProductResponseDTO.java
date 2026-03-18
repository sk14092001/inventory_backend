package com.inventory_backend.inventory_backend.dto;

public class ProductResponseDTO {

    private Long productId;
    private String description;
    private String name;
    private Double prefixPrice;
    private String unit;

    public ProductResponseDTO(Long productId, String description, String name, Double prefixPrice, String unit) {
        this.productId = productId;
        this.description = description;
        this.name = name;
        this.prefixPrice = prefixPrice;
        this.unit = unit;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrefixPrice() {
        return prefixPrice;
    }

    public void setPrefixPrice(Double prefixPrice) {
        this.prefixPrice = prefixPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
