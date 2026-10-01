package vn.iotstar.services;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(
            ProductRepository productRepository) {

        this.productRepository =
                productRepository;
    }


    // Lấy toàn bộ sản phẩm
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }


    // Tìm sản phẩm theo ID
    public Product getProductById(
            Integer id) {

        return productRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Không tìm thấy sản phẩm"
                    )
                );
    }
}