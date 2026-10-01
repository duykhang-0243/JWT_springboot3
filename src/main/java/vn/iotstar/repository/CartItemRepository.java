package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.CartItem;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Integer> {


    // Lấy toàn bộ giỏ hàng của một User
    List<CartItem> findByUser(User user);


    // Tìm xem sản phẩm đã tồn tại trong giỏ chưa
    Optional<CartItem> findByUserAndProduct(
            User user,
            Product product
    );
}