package com.xiangyou.about;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AboutController {

    public AboutController() {
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Principal principal, Model model) {
        return "about/about";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact(Principal principal, Model model) {
        return "about/contact";
    }

}
