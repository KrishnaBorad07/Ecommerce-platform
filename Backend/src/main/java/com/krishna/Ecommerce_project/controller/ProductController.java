package com.krishna.Ecommerce_project.controller;

import com.krishna.Ecommerce_project.model.Product;
import com.krishna.Ecommerce_project.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/")
    public String greet(){
        return "Hello world";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
       List<Product> products = productService.getAllProducts();
       if(products.isEmpty()){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(products);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
       Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try{
            Product product1 = productService.addProduct(product, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(product1);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProduct(@PathVariable int productId) {
        return productService.getProductById(productId)
                .map(product -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf(product.getImageType()))
                        .body(product.getImageData()))
                .orElse(ResponseEntity.notFound().build());
    }
}