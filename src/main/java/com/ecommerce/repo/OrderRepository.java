package com.ecommerce.repo;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

     Order findByUserAndStatus(User user, OrderStatus status);

     Order findByUserIdAndStatus(Long userId, OrderStatus status);

     List<Order> findAllByUserIdAndStatusIn(Long userId, List<OrderStatus> orderStatusList);

     List<Order> findAllByStatusIn(List<OrderStatus> orderStatusList);

    Optional<Order> findByTrackingId(UUID trackingId);

    @Query("SELECT COUNT(o) FROM Order o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month")
    Long countOrdersByMonthAndYear(int year, int month);

    @Query("SELECT SUM(o.amount) FROM Order o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month")
    BigDecimal sumEarningsByMonthAndYear(int year, int month);

    List<Order> findByDateBetweenAndStatus(Date startOfMonth, Date endOfMonth, OrderStatus status);

    Long countByStatus(OrderStatus status);

}
