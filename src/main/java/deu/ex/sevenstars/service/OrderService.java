package deu.ex.sevenstars.service;

import deu.ex.sevenstars.dto.OrderDTO;
import deu.ex.sevenstars.dto.PageRequestDTO;
import deu.ex.sevenstars.entity.OrderItem;
import deu.ex.sevenstars.entity.OrderStatus;
import deu.ex.sevenstars.entity.Orders;
import deu.ex.sevenstars.entity.Product;
import deu.ex.sevenstars.exception.OrderException;
import deu.ex.sevenstars.repository.OrderItemRepository;
import deu.ex.sevenstars.repository.OrderRepository;
import deu.ex.sevenstars.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderDTO insert(OrderDTO orderDTO) {
        try {
            String email = orderDTO.getEmail();

            // 이메일로 기존 주문 찾기 (없으면 null 반환)
            Orders existingOrder = orderRepository.findByEmail(email)
                    .orElse(null);

            Orders orders;
            if (existingOrder != null) {
                // 기존 주문이 있으면 그 주문을 사용
                orders = existingOrder;
            } else {
                // 기존 주문이 없으면 새로운 주문 생성
                orders = orderDTO.toEntity();
            }

            // 주문 상품 처리
            List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                    .map(productDTO -> {
                        // 상품 조회
                        Product product = productRepository.findById(productDTO.getProductId())
                                .orElseThrow(() -> new RuntimeException("Product not found"));

                        // 주문 아이템 생성
                        OrderItem orderItem = OrderItem.builder()
                                .product(product)
                                .category(product.getCategory())
                                .quantity(productDTO.getQuantity()) //수정부분
                                .price(product.getPrice())
                                .build();

                        orderItem.changeOrder(orders); // 주문 아이템에 주문 정보 설정
                        return orderItem;
                    })
                    .collect(Collectors.toList());

            orders.addOrderItems(orderItems);

            // 주문 저장
            Orders savedOrder = orderRepository.save(orders);
            return new OrderDTO(savedOrder);
        } catch (DataIntegrityViolationException e) {
            throw OrderException.NOT_FOUND.get();
        } catch (Exception e) {
            log.error("예외 발생 코드 : " + e.getMessage());
            throw OrderException.NOT_REGISTERED.get();
        }
    }


    public OrderDTO read(Long orderId){
        Orders orders = orderRepository.findById(orderId).orElseThrow(OrderException.NOT_FOUND::get);
        log.info(orders);
        log.info(orders.getOrderItems());
        return new OrderDTO(orders);
    }

    public OrderDTO update(OrderDTO orderDTO){
        Orders orders = orderRepository.findById(orderDTO.getOrderId()).orElseThrow(OrderException.NOT_FOUND::get);

        try {
            orders.changeAddress(orderDTO.getAddress());
            orders.changePostcode(orders.getPostcode());

            return new OrderDTO(orders);
        } catch (Exception e){
            log.error("예외 발생 코드 : "+e.getMessage());
            throw OrderException.NOT_MODIFIED.get();
        }
    }

    public void delete(Long orderId){
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(OrderException.NOT_FOUND::get);
        try {
            orderRepository.delete(orders);
        } catch (Exception e){
            log.error("예외 발생 코드 : " + e.getMessage());
            throw OrderException.NOT_REMOVED.get();
        }
    }

    public Page<OrderDTO> page(PageRequestDTO pageRequestDTO){
        try {
            Sort sort = Sort.by("orderId").ascending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            return orderRepository.listDTO(pageable);
        }catch (Exception e){
            log.error("예외 코드 : " + e.getMessage());
            throw OrderException.NOT_FOUND.get();
        }
    }

    // 전날 오후 2시부터 당일 오후 2시 상품 처리
    public void updateOrderStatus() {

        LocalDateTime startTime = LocalDateTime.now().minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTime = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0);


        List<Orders> ordersToUpdate = orderRepository.findByCreatedAtBetweenAndOrderStatus(
                startTime, endTime, OrderStatus.ACCEPTED
        );

        ordersToUpdate.forEach(order -> {
            order.changeOrderStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
        });
    }
}