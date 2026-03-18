package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.ProductRequest;
import com.inventory_backend.inventory_backend.dto.ProductResponse;
import com.inventory_backend.inventory_backend.dto.ProductResponseDTO;
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


    public List<ProductResponseDTO> getAllProducts() {
        List<ProductResponseDTO> list = productRepository.getProductDetails();
//        List<ProductResponseDTO> responseDTO = new ArrayList<>();
//        for(Object[] obj: list){
//            responseDTO.add(new ProductResponseDTO(
//                    ((Number) obj[0]).longValue(),   // product_id
//                    (String) obj[1],                 // description
//                    (String) obj[2],                 // name
//                    ((Number) obj[3]).doubleValue(), // prefix_price
//                    (String) obj[4]
//            ));
//        }

//        for (Product p : list) {
//            responseList.add(
//                    new ProductResponse(
//                            p.getProductId(),
//                            p.getName(),
//                            p.getUnit(),
//                            p.getDescription(),
//                            p.getPrefixPrice()
//                    )
//            );
//        }

        return list;
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

    public ProductResponse update(Long id,ProductRequest req)
    {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(req.getName());
        product.setUnit(req.getUnit());
        product.setDescription(req.getDescription());
        product.setPrefixPrice(req.getPrefixPrice());
        Product savedProduct  = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getProductId(),
                savedProduct.getName(),
                savedProduct.getUnit(),
                savedProduct.getDescription(),
                savedProduct.getPrefixPrice()
        );
    }

    public String delete(Long id)
    {
        productRepository.deleteById(id);
        return "Product Deleted Successfully";
    }
}

