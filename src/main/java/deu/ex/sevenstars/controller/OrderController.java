package deu.ex.sevenstars.controller;

import deu.ex.sevenstars.dto.OrderDTO;
import deu.ex.sevenstars.dto.PageRequestDTO;
import deu.ex.sevenstars.exception.OrderException;
import deu.ex.sevenstars.service.OrderService;
import deu.ex.sevenstars.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order Controller", description = "Order Controller API")
public class OrderController {
    private final ProductService productService;
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 등록", description = "주문 등록하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<OrderDTO> register(
            @Validated @RequestBody OrderDTO orderDTO
    ){
        log.info("--- register()");
        log.info("--- orderDTO : "+orderDTO);

        return ResponseEntity.ok(orderService.insert(orderDTO));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 1개 읽기", description = "주문 정보 1개 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    @Parameters(@Parameter(name = "orderId", description = "주문번호"))
    public ResponseEntity<OrderDTO> read(
            @PathVariable ("orderId") Long orderId
    ){
        log.info("--- read()");
        log.info("--- orderId : " + orderId);

        return ResponseEntity.ok(orderService.read(orderId));
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "주문 수정", description = "주문 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = @Content(mediaType = "application/json"))}
    )
    @Parameters(@Parameter(name = "orderId", description = "주문번호"))
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
    @Operation(summary = "주문 삭제", description = "주문 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json"))}
    )
    @Parameters(@Parameter(name = "orderId", description = "주문번호"))
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
    @Operation(summary = "주문 목록", description = "주문 목록 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<Page<OrderDTO>> page(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(orderService.page(pageRequestDTO));
    }

}
