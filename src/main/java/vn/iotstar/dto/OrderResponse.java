package vn.iotstar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Integer id;

    private String receiverName;

    private String phone;

    private String address;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private String status;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;


    public OrderResponse() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(
            String receiverName) {

        this.receiverName = receiverName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone) {

        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(
            String address) {

        this.address = address;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
            BigDecimal totalAmount) {

        this.totalAmount = totalAmount;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod) {

        this.paymentMethod = paymentMethod;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status) {

        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }


    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(
            List<OrderItemResponse> items) {

        this.items = items;
    }
}