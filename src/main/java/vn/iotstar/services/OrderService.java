package vn.iotstar.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.dto.CheckoutRequest;
import vn.iotstar.entity.CartItem;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderItem;
import vn.iotstar.entity.OrderStatus;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CartItemRepository;
import vn.iotstar.repository.OrderItemRepository;
import vn.iotstar.repository.OrderRepository;
import vn.iotstar.repository.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository) {

        this.orderRepository =
                orderRepository;

        this.orderItemRepository =
                orderItemRepository;

        this.cartItemRepository =
                cartItemRepository;

        this.productRepository =
                productRepository;
    }


    // =====================================================
    // CHECKOUT COD
    // =====================================================

    @Transactional
    public Order checkoutCOD(
            User user,
            CheckoutRequest request) {


        // =================================================
        // 1. KIỂM TRA THÔNG TIN NHẬN HÀNG
        // =================================================

        if (request == null) {

            throw new RuntimeException(
                    "Thông tin đặt hàng không hợp lệ."
            );
        }


        if (request.getReceiverName() == null
                || request.getReceiverName()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Vui lòng nhập tên người nhận."
            );
        }


        if (request.getPhone() == null
                || request.getPhone()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Vui lòng nhập số điện thoại."
            );
        }


        if (request.getAddress() == null
                || request.getAddress()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Vui lòng nhập địa chỉ nhận hàng."
            );
        }


        // =================================================
        // 2. LẤY GIỎ HÀNG CỦA USER
        // =================================================

        List<CartItem> cartItems =
                cartItemRepository.findByUser(
                        user
                );


        if (cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Giỏ hàng đang trống."
            );
        }


        // =================================================
        // 3. KIỂM TRA STOCK + TÍNH TỔNG TIỀN
        // =================================================

        BigDecimal totalAmount =
                BigDecimal.ZERO;


        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            Integer quantity =
                    cartItem.getQuantity();


            if (quantity == null
                    || quantity < 1) {

                throw new RuntimeException(
                        "Số lượng sản phẩm không hợp lệ."
                );
            }


            if (product.getStock() < quantity) {

                throw new RuntimeException(
                        "Sản phẩm "
                        + product.getName()
                        + " không đủ tồn kho."
                );
            }


            BigDecimal subtotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            quantity
                                    )
                            );


            totalAmount =
                    totalAmount.add(
                            subtotal
                    );
        }


        // =================================================
        // 4. TẠO ORDER
        // =================================================

        Order order =
                new Order();


        order.setUser(
                user
        );


        order.setReceiverName(
                request.getReceiverName()
                        .trim()
        );


        order.setPhone(
                request.getPhone()
                        .trim()
        );


        order.setAddress(
                request.getAddress()
                        .trim()
        );


        order.setTotalAmount(
                totalAmount
        );


        order.setPaymentMethod(
                "COD"
        );


        order.setStatus(
                OrderStatus.NEW
        );


        order.setCreatedAt(
                LocalDateTime.now()
        );


        order =
                orderRepository.save(
                        order
                );


        // =================================================
        // 5. TẠO ORDER ITEM + TRỪ STOCK
        // =================================================

        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            Integer quantity =
                    cartItem.getQuantity();


            /*
             * Lưu giá tại thời điểm khách đặt hàng.
             */

            OrderItem orderItem =
                    new OrderItem(
                            order,
                            product,
                            product.getPrice(),
                            quantity
                    );


            orderItemRepository.save(
                    orderItem
            );


            /*
             * Trừ tồn kho.
             */

            product.setStock(
                    product.getStock()
                    - quantity
            );


            productRepository.save(
                    product
            );
        }


        // =================================================
        // 6. XÓA GIỎ HÀNG
        // =================================================

        cartItemRepository.deleteByUser(
                user
        );


        // =================================================
        // 7. TRẢ VỀ ORDER
        // =================================================

        return order;
    }
}