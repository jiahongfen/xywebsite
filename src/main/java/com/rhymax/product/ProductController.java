package com.rhymax.product;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    ServletContext context;
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/product/features", method = RequestMethod.GET)
    public String showFeatures(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id == null) {
            return "redirect:/";
        }
        Product product = productRepository.getProduct(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "/product/features";
    }

    @RequestMapping(value = "/product/details", method = RequestMethod.GET)
    public String showDetails(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id == null) {
            return "redirect:/";
        }
        Product product = productRepository.getProduct(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "/product/details";
    }
}
