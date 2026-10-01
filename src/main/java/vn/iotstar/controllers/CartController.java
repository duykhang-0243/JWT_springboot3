package vn.iotstar.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.entity.CartItem;
import vn.iotstar.entity.User;
import vn.iotstar.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;


    public CartController(
            CartService cartService) {

        this.cartService = cartService;
    }


    // =====================================================
    // 1. XEM GIỎ HÀNG
    // GET /cart
    // =====================================================

    @GetMapping
    public ResponseEntity<List<CartItem>>
            getCart(Authentication authentication) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                cartService.getCart(user)
        );
    }


    // =====================================================
    // 2. THÊM SẢN PHẨM
    // POST /cart/add/{productId}
    // =====================================================

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartItem>
            addToCart(
                    @PathVariable Integer productId,
                    Authentication authentication) {

        User user =
                (User) authentication.getPrincipal();

        CartItem cartItem =
                cartService.addToCart(
                        user,
                        productId
                );

        return ResponseEntity.ok(
                cartItem
        );
    }


    // =====================================================
    // 3. SỬA SỐ LƯỢNG
    // PUT /cart/{cartItemId}?quantity=3
    // =====================================================

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem>
            updateQuantity(
                    @PathVariable Integer cartItemId,
                    @RequestParam Integer quantity,
                    Authentication authentication) {

        User user =
                (User) authentication.getPrincipal();

        CartItem cartItem =
                cartService.updateQuantity(
                        user,
                        cartItemId,
                        quantity
                );

        return ResponseEntity.ok(
                cartItem
        );
    }


    // =====================================================
    // 4. XÓA SẢN PHẨM
    // DELETE /cart/{cartItemId}
    // =====================================================

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void>
            removeItem(
                    @PathVariable Integer cartItemId,
                    Authentication authentication) {

        User user =
                (User) authentication.getPrincipal();

        cartService.removeItem(
                user,
                cartItemId
        );

        return ResponseEntity.noContent().build();
    }
}