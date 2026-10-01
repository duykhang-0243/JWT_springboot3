package vn.iotstar.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false, length = 150)
    private String name;


    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;


    @Column(nullable = false)
    private Integer stock;


    @Column(length = 500)
    private String image;


    // Constructor rỗng cho JPA
    public Product() {
    }


    public Product(
            String name,
            BigDecimal price,
            Integer stock,
            String image) {

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Integer getStock() {
        return stock;
    }


    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }
}