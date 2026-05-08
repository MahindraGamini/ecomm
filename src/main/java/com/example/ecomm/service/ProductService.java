package com.example.ecomm.service;

import com.example.ecomm.model.Product;
import com.example.ecomm.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

@Autowired
private ProductRepo productRepo;
    public List<Product> getProducts(){
        return productRepo.findAll();
    }
    public Product getProductById(int id){
        return productRepo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        System.out.println(product);
        return productRepo.save(product);


    }

    public Product updateProduct(int id,Product product,MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return productRepo.save(product);
    }
    public List<Product> search(String keyword){
        return productRepo.searchProduct(keyword);
    }
}
