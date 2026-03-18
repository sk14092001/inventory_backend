
package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductResponse {

    private Long ProductId;

    private String name;

    private String unit;

    private String description;

    private Double prefixPrice;


}
