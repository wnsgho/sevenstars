package deu.ex.sevenstars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductViewController {

    @GetMapping("/products")
    public String listProducts() {
        return "product-list"; // 템플릿 이름 (templates/product-list.html)
    }

    @GetMapping("/new-product")
    public String newProduct () {
        return "new-product"; // 템플릿 이름 (templates/product-list.html)
    }

}
