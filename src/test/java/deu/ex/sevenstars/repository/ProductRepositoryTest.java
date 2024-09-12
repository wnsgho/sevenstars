package deu.ex.sevenstars.repository;

import deu.ex.sevenstars.dto.ProductDTO;
import deu.ex.sevenstars.entity.Category;
import deu.ex.sevenstars.entity.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation =  Propagation.NOT_SUPPORTED)
@Log4j2
@AutoConfigureMockMvc
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test //inset
    public void testInsert(){
        // GIVEN
        IntStream.rangeClosed(1, 15).forEach(i -> {
                    Product product = Product.builder()
                            .productName("상품 " + i)
                            .category(Category.COFFEE_BEAN_PACKAGE)
                            .price(1000*i)
                            .description("테스트 원두 " + i)
                            .build();


                    //WHEN
                    Product savedProduct = productRepository.save(product);

                    //THEN
                    assertNotNull(savedProduct);
                    assertEquals(i, savedProduct.getProductId());
                }
        );
    }

    @Test
    public void testRead(){ //select
        Long productId = 1L;
        Optional<Product> foundProduct = productRepository.findById(productId);
        assertTrue(foundProduct.isPresent(),"Product should be present");
        //있는지 검증
//        System.out.println("---------------");
//        System.out.println(foundProduct);
        assertNotNull(foundProduct);
    }



    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        Long no=1L;
        String name="수정";
        int price = 9500;

        Optional<Product> foundProduct = productRepository.findById(no);
        assertTrue(foundProduct.isPresent(),"Product should be present");
        Product product = foundProduct.get();

        product.changeProductName(name);
        product.changePrice(price);

        assertEquals(foundProduct.get().getProductName(),product.getProductName());
        assertEquals(foundProduct.get().getPrice(),product.getPrice());
    }

    @Test
    public void testDelete(){
        Long no=1L;

        Optional<Product> foundProduct = productRepository.findById(no);
        assertTrue(foundProduct.isPresent(),"Product should be present");
        //있는지 검증

        Product product = foundProduct.get();
        productRepository.delete(product);

        assertFalse(productRepository.findById(no).isPresent());

    }

    @Test
    public void testList(){ //List로 받기
        List<Product> productList = productRepository.findAll();

        System.out.println(productList.size());
        productList.forEach(System.out::println);
    }

    @Test
    public void testListPage(){//page로 받기
        Pageable pageable = PageRequest.of(0,10, Sort.by("productId").descending());

        Page<Product> productPage = productRepository.list(pageable);
        assertNotNull(productPage);
        assertEquals(15,productPage.getTotalElements());
        assertEquals(2,productPage.getTotalPages());
        assertEquals(0,productPage.getNumber());
        assertEquals(10,productPage.getSize());
        assertEquals(10,productPage.getContent().size());

        productPage.getContent().forEach(System.out::println);
    }

    @Test
    public void testListPageTwo(){//page로 받기
        Pageable pageable = PageRequest.of(0,10, Sort.by("price").ascending());

        Page<Product> productPage = productRepository.findProductsByOrderByPriceASC(pageable);
        assertNotNull(productPage);
//        assertEquals(15,productPage.getTotalElements());
//        assertEquals(2,productPage.getTotalPages());
//        assertEquals(0,productPage.getNumber());
//        assertEquals(10,productPage.getSize());
//        assertEquals(10,productPage.getContent().size());

        productPage.getContent().forEach(System.out::println);
    }

}
