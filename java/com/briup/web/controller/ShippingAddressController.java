package com.briup.web.controller;

import com.briup.bean.ShippingAddress;
import com.briup.bean.User;
import com.briup.service.IShippingAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class ShippingAddressController {
    @Autowired
    private IShippingAddressService shippingAddressService;

    @GetMapping("/toAddShippingAddress")
    public String toAddShippingAddress(Model model, HttpSession session){
        User user= (User) session.getAttribute("user");
        List<ShippingAddress> shippingAddresses=
                shippingAddressService.findUserAllShippingAddress(user.getId());
        model.addAttribute("addressList",shippingAddresses);
        return "addShippingAddress";
    }

    @PostMapping("/addShippingAddress")
    public void addShippingAddress(ShippingAddress shippingAddress, HttpServletResponse response, HttpSession session) throws IOException {
        User user= (User) session.getAttribute("user");
        shippingAddress.setUser(user);
        shippingAddressService.saveShippingAddress(shippingAddress);
        response.sendRedirect("toAddShippingAddress");
    }
}
