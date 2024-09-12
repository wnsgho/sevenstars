package deu.ex.sevenstars.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import deu.ex.sevenstars.dto.PageRequestDTO;
import deu.ex.sevenstars.dto.ProductDTO;
import deu.ex.sevenstars.entity.Product;
import deu.ex.sevenstars.exception.ProductException;
import deu.ex.sevenstars.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "Product Controller API")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/register")
    @Operation(summary = "상품 등록", description = "상품 등록하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<ProductDTO> register(
            @RequestPart("productDTO") String productDTOJson,
            @RequestPart("imageFile") MultipartFile imageFile) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);

        ProductDTO savedProduct = productService.insert(productDTO, imageFile);
        return ResponseEntity.ok(savedProduct);
    }



    @GetMapping("/{productId}")
    @Operation(summary = "상품 1개 읽기", description = "상품 1개만 정보만 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    @Parameters(@Parameter(name = "productId", description = "상품번호"))
    public ResponseEntity<ProductDTO> read(
            @PathVariable ("productId") Long productId
    ){
        log.info("--- read()");
        log.info("--- productId : " + productId);

        return ResponseEntity.ok(productService.read(productId));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = @Content(mediaType = "application/json"))}
    )
    @Parameters(@Parameter(name = "productId", description = "상품번호"))
    public ResponseEntity<ProductDTO> modify(
            @PathVariable ("productId") Long productId,
            @Validated @RequestPart ProductDTO productDTO,
            @RequestPart(required = false) MultipartFile imageFile
    ){
        log.info("--- modify()");
        log.info(("--- productDTO : " + productDTO));

        if(!productId.equals(productDTO.getProductId())){
            throw ProductException.NOT_MODIFIED.get();
        }

        return ResponseEntity.ok(productService.update(productDTO, imageFile));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json"))}
    )
    @Parameters(@Parameter(name = "productId", description = "상품번호"))
    public ResponseEntity<Map<String,String >> remove (
            @PathVariable ("productId") Long productId){
        log.info("--- remove()");
        log.info("--- productId : " + productId);

        try {
            productService.delete(productId);
        }catch (Exception e){
            log.error("예외 발생 코드 : " + e.getMessage());
            throw ProductException.NOT_REMOVED.get();
        }

        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping
    @Operation(summary = "상품 목록", description = "상품 목록을 가져오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<Page<ProductDTO>> page(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.page(pageRequestDTO));
    }

    @GetMapping("/price/ASC")
    @Operation(summary = "상품 목록 가격 오름차순 정렬", description = "상품 가격 오름차순 정렬로 보여주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<Page<Product>> pagePriceASC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pagePriceASC(pageRequestDTO));
    }

    @GetMapping("/price/DESC")
    @Operation(summary = "상품 목록 가격 내림차순 정렬", description = "상품 가격 내림차순 정렬로 보여주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<Page<Product>> pagePriceDESC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pagePriceDESC(pageRequestDTO));
    }

    @GetMapping("/category/ASC")
    @Operation(summary = "상품 목록 카테고리 오름차순 정렬", description = "상품 카테고리 오름차순 정렬로 보여주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<Page<Product>> pageCategoryASC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pageCategoryASC(pageRequestDTO));
    }

    @GetMapping("/category/DESC")
    @Operation(summary = "상품 목록 카테고리 내림차순 정렬", description = "상품 카테고리 내림차순 정렬로 보여주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json"))}
    )
    public ResponseEntity<Page<Product>> pageCategoryDESC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pageCategoryDESC(pageRequestDTO));
    }

//    @PostMapping("/products")
//    public ResponseEntity<ProductDTO> add(@Validated @RequestBody ProductDTO productDTO){
//        log.info("--- add()" + productDTO);
//
//        return ResponseEntity.ok()
//    }

}
