package com.rhymax.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {

    private static final String ORDER_VIEW_NAME = "product/order";
    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(value = "/product/order", method = RequestMethod.GET)
    public String order(@RequestParam(value = "id", required = false, defaultValue = "0") int id, Model model) {
        model.addAttribute(new OrderForm());
        return "/product/order";
    }

    @RequestMapping(value = "/product/doOrder", method = RequestMethod.POST)
    public String doOrder(@Valid @ModelAttribute OrderForm orderForm, Errors errors, RedirectAttributes ra) {
        if (errors.hasErrors()) {
            System.out.println("Has error " + errors);
            return ORDER_VIEW_NAME;
        }
        Ordar order = orderRepository.save(orderForm.createOrder());
        return "redirect:/product/orderSuccess?orderId=" + order.getId();
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
