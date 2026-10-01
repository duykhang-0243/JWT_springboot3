package vn.iotstar.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // =====================================================
    // ORDER
    // =====================================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
        name = "order_id",
        nullable = false
    )
    private Order order;


    // =====================================================
    // PRODUCT
    // =====================================================

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(
        name = "product_id",
        nullable = false
    )
    private Product product;


    // =====================================================
    // GIÁ TẠI THỜI ĐIỂM ĐẶT HÀNG
    // =====================================================

    @Column(
        name = "price",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal price;


    // =====================================================
    // SỐ LƯỢNG
    // =====================================================

    @Column(
        name = "quantity",
        nullable = false
    )
    private Integer quantity;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderItem() {
    }


    public OrderItem(
        Order order,
        Product product,
        BigDecimal price,
        Integer quantity
    ) {

        this.order = order;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }


    // =====================================================
    // GETTER / SETTER
    // =====================================================

    public Integer getId() {
        return id;
    }

    public void setId(
        Integer id
    ) {
        this.id = id;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(
        Order order
    ) {
        this.order = order;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(
        Product product
    ) {
        this.product = product;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(
        BigDecimal price
    ) {
        this.price = price;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(
        Integer quantity
    ) {
        this.quantity = quantity;
    }
}