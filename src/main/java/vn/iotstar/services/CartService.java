package vn.iotstar.services;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.iotstar.entity.CartItem;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CartItemRepository;
import vn.iotstar.repository.ProductRepository;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    public CartService(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    // =====================================================
    // 1. LẤY GIỎ HÀNG CỦA USER
    // =====================================================

    public List<CartItem> getCart(User user) {

        return cartItemRepository.findByUser(user);
    }


    // =====================================================
    // 2. THÊM SẢN PHẨM VÀO GIỎ
    // =====================================================

    public CartItem addToCart(
            User user,
            Integer productId) {

        // Tìm sản phẩm
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                            () -> new RuntimeException(
                                "Không tìm thấy sản phẩm"
                            )
                        );


        // Không còn hàng
        if (product.getStock() <= 0) {

            throw new RuntimeException(
                "Sản phẩm đã hết hàng"
            );
        }


        // Kiểm tra sản phẩm đã có trong giỏ chưa
        CartItem cartItem =
                cartItemRepository
                        .findByUserAndProduct(
                            user,
                            product
                        )
                        .orElse(null);


        // ---------------------------------------------
        // CHƯA CÓ TRONG GIỎ
        // ---------------------------------------------

        if (cartItem == null) {

            cartItem =
                    new CartItem(
                        user,
                        product,
                        1
                    );

        }

        // ---------------------------------------------
        // ĐÃ CÓ TRONG GIỎ
        // → tăng quantity
        // ---------------------------------------------

        else {

            int newQuantity =
                    cartItem.getQuantity() + 1;


            // Không vượt tồn kho
            if (newQuantity > product.getStock()) {

                throw new RuntimeException(
                    "Số lượng vượt quá tồn kho"
                );
            }


            cartItem.setQuantity(
                newQuantity
            );
        }


        return cartItemRepository.save(
            cartItem
        );
    }


    // =====================================================
    // 3. SỬA SỐ LƯỢNG
    // =====================================================

    public CartItem updateQuantity(
            User user,
            Integer cartItemId,
            Integer quantity) {


        // ---------------------------------------------
        // Quantity phải >= 1
        // ---------------------------------------------

        if (quantity == null || quantity < 1) {

            throw new RuntimeException(
                "Số lượng phải lớn hơn hoặc bằng 1"
            );
        }


        // Tìm CartItem
        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(
                            () -> new RuntimeException(
                                "Không tìm thấy sản phẩm trong giỏ"
                            )
                        );


        // ---------------------------------------------
        // Kiểm tra CartItem có thuộc User không
        // ---------------------------------------------

        if (!cartItem
                .getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                "Bạn không có quyền sửa sản phẩm này"
            );
        }


        Product product =
                cartItem.getProduct();


        // ---------------------------------------------
        // Không vượt tồn kho
        // ---------------------------------------------

        if (quantity > product.getStock()) {

            throw new RuntimeException(
                "Số lượng tối đa là "
                + product.getStock()
            );
        }


        cartItem.setQuantity(
            quantity
        );


        return cartItemRepository.save(
            cartItem
        );
    }


    // =====================================================
    // 4. XÓA SẢN PHẨM KHỎI GIỎ
    // =====================================================

    public void removeItem(
            User user,
            Integer cartItemId) {


        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(
                            () -> new RuntimeException(
                                "Không tìm thấy sản phẩm trong giỏ"
                            )
                        );


        // ---------------------------------------------
        // Kiểm tra quyền sở hữu
        // ---------------------------------------------

        if (!cartItem
                .getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                "Bạn không có quyền xóa sản phẩm này"
            );
        }


        cartItemRepository.delete(
            cartItem
        );
    }
}