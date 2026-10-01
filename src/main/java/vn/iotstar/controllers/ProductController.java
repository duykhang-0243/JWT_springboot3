package vn.iotstar.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.entity.Product;
import vn.iotstar.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(
            ProductService productService) {

        this.productService =
                productService;
    }


    // GET /products
    @GetMapping
    public ResponseEntity<List<Product>>
            getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }
}