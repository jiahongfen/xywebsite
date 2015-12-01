package com.xiangyou.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiangyou.account.Tourist;

@Controller
public class OrderController {
    static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public static final String ATTR_PRODUCT = "product";

    private static final String ORDER_VIEW_NAME = "product/order";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/product/order", method = RequestMethod.GET)
    public String order(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id == null) {
            return "redirect:/";
        }
        Product product = productRepository.getProduct(id);
        if (product == null) {
            return "redirect:/";
        }
        OrderForm orderForm = new OrderForm();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderForm.setProductId(product.getId());
        orderForm.setPhone(username);
        model.addAttribute(orderForm);
        model.addAttribute(ATTR_PRODUCT, product);
        return "/product/order";
    }

    @RequestMapping(value = "/product/doOrder", method = RequestMethod.POST)
    public String doOrder(@Valid @ModelAttribute OrderForm orderForm, Errors errors, RedirectAttributes ra,
            HttpServletRequest request, Model model) throws JsonProcessingException {
        if (errors.hasErrors()) {
            System.out.println("Has error " + errors);
            return ORDER_VIEW_NAME;
        }
        getTourists(orderForm, request);
        Ordar order = orderForm.createOrder();
        order.setUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        order = orderRepository.save(order);
        model.addAttribute("order", order);
        return "/product/orderSuccess";
    }

    private void getTourists(OrderForm orderForm, HttpServletRequest request) {
        int touristsCount = NumberUtils.toInt(orderForm.getAdults()) + NumberUtils.toInt(orderForm.getKids());
        for (int i = 0; i < touristsCount; i++) {
            orderForm.getTourists().add(getTourist(request, i));
        }
    }

    private Tourist getTourist(HttpServletRequest request, int i) {
        return Tourist.newPassportTourist(request.getParameter("name" + i), request.getParameter("number" + i));
    }

    @RequestMapping(value = "/product/orderSuccess", method = RequestMethod.GET)
    public String showOrderSuccess(@RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,
            Model model) {
        model.addAttribute("orderId", orderId);
        return "/product/orderSuccess";
    }

    @RequestMapping(value = "/product/orderList", method = RequestMethod.GET)
    public String showOrderList(Model model) {
        List<Ordar> orderList = orderRepository.queryAll();
        model.addAttribute("orderList", orderList);
        return "/product/orderList";
    }
}
