package vn.iotstar.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import vn.iotstar.dto.CheckoutRequest;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.User;
import vn.iotstar.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    public OrderController(
            OrderService orderService) {

        this.orderService =
                orderService;
    }


    // =====================================================
    // THANH TOÁN COD
    // =====================================================

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutCOD(
            @RequestBody CheckoutRequest request,
            Authentication authentication) {

        try {

            User user =
                    (User) authentication
                            .getPrincipal();


            Order order =
                    orderService.checkoutCOD(
                            user,
                            request
                    );


            return ResponseEntity.ok(
                    order
            );

        }
        catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}