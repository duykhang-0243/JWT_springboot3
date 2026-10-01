package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderStatus;
import vn.iotstar.entity.User;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Integer> {

    // Tất cả đơn hàng của user
    List<Order> findByUserOrderByCreatedAtDesc(
            User user
    );


    // Lọc đơn hàng theo trạng thái
    List<Order> findByUserAndStatusOrderByCreatedAtDesc(
            User user,
            OrderStatus status
    );
}