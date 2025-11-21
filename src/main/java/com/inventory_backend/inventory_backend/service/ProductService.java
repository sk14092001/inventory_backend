package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.ProductRequest;
import com.inventory_backend.inventory_backend.dto.ProductResponse;
import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public ProductResponse saveProduct(ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .description(request.getDescription())
                .prefixPrice(request.getPrefixPrice())
                .build();

        Product saved = productRepository.save(product);


        return new ProductResponse(
                saved.getProductId(),
                saved.getName(),
                saved.getUnit(),
                saved.getDescription(),
                saved.getPrefixPrice()
        );
    }


    public List<ProductResponse> getAllProducts() {
        List<Product> list = productRepository.findAll();

        List<ProductResponse> responseList = new ArrayList<>();

        for (Product p : list) {
            responseList.add(
                    new ProductResponse(
                            p.getProductId(),
                            p.getName(),
                            p.getUnit(),
                            p.getDescription(),
                            p.getPrefixPrice()
                    )
            );
        }

        return responseList;
    }


    public ProductResponse getProduct(Long id) {

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return new ProductResponse(
                p.getProductId(),
                p.getName(),
                p.getUnit(),
                p.getDescription(),
                p.getPrefixPrice()
        );
    }

    public ProductResponse update(Long id,ProductRequest request)
    {
        Product product  =productRepository.findById(id).
                orElseThrow(()->new RuntimeException("Product Not Found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrefixPrice(request.getPrefixPrice());
        product.setUnit(request.getUnit());
        Product updatedProduct =productRepository.save(product);
        return new ProductResponse(
                updatedProduct.getProductId(),
                updatedProduct.getName(),
                updatedProduct.getUnit(),
                updatedProduct.getDescription(),
                updatedProduct.getPrefixPrice()
        );
    }

    public String delete(Long id)
    {
        productRepository.deleteById(id);
        return "Product Deleted Successfully";
    }
}

