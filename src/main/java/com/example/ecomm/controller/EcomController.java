package com.example.ecomm.controller;

import com.example.ecomm.model.Product;
import com.example.ecomm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class EcomController
{
    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public List<Product> getProducts()
    {
        return productService.getProducts();
    }
    @GetMapping("/product/{id}")
    public  Product getProduct( @PathVariable  int id)
    {
        return productService.getProductById(id);
    }

    @PostMapping(value = "/product")
    public ResponseEntity<?> addProduct(
            @RequestPart String productJson,
            @RequestPart  MultipartFile image) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(productJson, Product.class);

            Product saved = productService.addProduct(product, image);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
