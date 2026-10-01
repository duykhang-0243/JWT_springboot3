package vn.iotstar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // =====================================================
    // USER ĐẶT HÀNG
    // =====================================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;


    // =====================================================
    // THÔNG TIN NHẬN HÀNG
    // =====================================================

    @Column(
        name = "receiver_name",
        nullable = false,
        length = 100
    )
    private String receiverName;


    @Column(
        name = "phone",
        nullable = false,
        length = 20
    )
    private String phone;


    @Column(
        name = "address",
        nullable = false,
        length = 500
    )
    private String address;


    // =====================================================
    // TỔNG TIỀN
    // =====================================================

    @Column(
        name = "total_amount",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal totalAmount;


    // =====================================================
    // PHƯƠNG THỨC THANH TOÁN
    // =====================================================

    @Column(
        name = "payment_method",
        nullable = false,
        length = 20
    )
    private String paymentMethod;


    // =====================================================
    // TRẠNG THÁI
    // =====================================================

    @Enumerated(EnumType.STRING)

    @Column(
        name = "status",
        nullable = false,
        length = 30
    )
    private OrderStatus status;


    // =====================================================
    // THỜI GIAN ĐẶT
    // =====================================================

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Order() {
    }


    // =====================================================
    // GETTER / SETTER
    // =====================================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(
        String receiverName
    ) {
        this.receiverName = receiverName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(
        String phone
    ) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(
        String address
    ) {
        this.address = address;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
        BigDecimal totalAmount
    ) {
        this.totalAmount = totalAmount;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
        String paymentMethod
    ) {
        this.paymentMethod = paymentMethod;
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(
        OrderStatus status
    ) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
        LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }
}