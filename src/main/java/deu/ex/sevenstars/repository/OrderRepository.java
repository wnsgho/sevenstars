package deu.ex.sevenstars.repository;

import deu.ex.sevenstars.entity.OrderStatus;
import deu.ex.sevenstars.entity.Orders;
import deu.ex.sevenstars.repository.search.OrderSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders,Long>, OrderSearch {

    Optional<Orders> findByEmail(String email);
    List<Orders> findByCreatedAtBetweenAndOrderStatus(LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   OrderStatus status);
}
