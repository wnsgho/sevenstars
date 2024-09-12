package deu.ex.sevenstars.controller;

import deu.ex.sevenstars.dto.OrderDTO;
import deu.ex.sevenstars.dto.PageRequestDTO;
import deu.ex.sevenstars.dto.ProductDTO;
import deu.ex.sevenstars.exception.OrderException;
import deu.ex.sevenstars.exception.ProductException;
import deu.ex.sevenstars.service.OrderService;
import deu.ex.sevenstars.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final ProductService productService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> register(
            @Validated @RequestBody OrderDTO orderDTO
    ){
        log.info("--- register()");
        log.info("--- orderDTO : "+orderDTO);

        return ResponseEntity.ok(orderService.insert(orderDTO));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> read(
            @PathVariable ("orderId") Long orderId
    ){
        log.info("--- read()");
        log.info("--- orderId : " + orderId);

        return ResponseEntity.ok(orderService.read(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> modify(
            @PathVariable ("orderId") Long orderId,
            @Validated @RequestBody OrderDTO orderDTO
    ){
        log.info("--- modify()");
        log.info(("--- orderDTO : " + orderDTO));

        if(!orderId.equals(orderDTO.getOrderId())){
            throw OrderException.NOT_MODIFIED.get();
        }

        return ResponseEntity.ok(orderService.update(orderDTO));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String,String >> remove (
            @PathVariable ("orderId") Long orderId){
        log.info("--- remove()");
        log.info("--- orderId : " + orderId);

        try {
            orderService.delete(orderId);
        }catch (Exception e){
            log.error("예외 발생 코드 : " + e.getMessage());
            throw OrderException.NOT_REMOVED.get();
        }

        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> page(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(orderService.page(pageRequestDTO));
    }

}
