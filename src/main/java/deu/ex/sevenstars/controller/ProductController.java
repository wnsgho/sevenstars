package deu.ex.sevenstars.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import deu.ex.sevenstars.dto.PageRequestDTO;
import deu.ex.sevenstars.dto.ProductDTO;
import deu.ex.sevenstars.entity.Product;
import deu.ex.sevenstars.exception.ProductException;
import deu.ex.sevenstars.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<ProductDTO> register(
            @RequestPart("productDTO") String productDTOJson,
            @RequestPart("imageFile") MultipartFile imageFile) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);

        ProductDTO savedProduct = productService.insert(productDTO, imageFile);
        return ResponseEntity.ok(savedProduct);
    }



    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> read(
            @PathVariable ("productId") Long productId
    ){
        log.info("--- read()");
        log.info("--- productId : " + productId);

        return ResponseEntity.ok(productService.read(productId));
    }

    @PutMapping("/{productId}")
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
    public ResponseEntity<Page<ProductDTO>> page(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.page(pageRequestDTO));
    }

    @GetMapping("/price/ASC")
    public ResponseEntity<Page<Product>> pagePriceASC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pagePriceASC(pageRequestDTO));
    }

    @GetMapping("/price/DESC")
    public ResponseEntity<Page<Product>> pagePriceDESC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pagePriceDESC(pageRequestDTO));
    }

    @GetMapping("/category/ASC")
    public ResponseEntity<Page<Product>> pageCategoryASC(@Validated PageRequestDTO pageRequestDTO){
        log.info("page() : " + pageRequestDTO);

        return ResponseEntity.ok(productService.pageCategoryASC(pageRequestDTO));
    }

    @GetMapping("/category/DESC")
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
