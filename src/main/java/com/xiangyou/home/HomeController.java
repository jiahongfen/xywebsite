package com.xiangyou.home;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiangyou.product.Product;
import com.xiangyou.product.ProductRepository;
import com.xiangyou.signup.SignupForm;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Principal principal, Model model) {
        List<Product> productList = new ArrayList<Product>(productRepository.getProducts().values());
        model.addAttribute("productList", productList);
        if (principal != null) {
            return "home/home";
            // return "home/homeSignedIn";
        } else {
            model.addAttribute(new SignupForm());
            return "home/home";
        }
    }
}
