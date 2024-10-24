package com.briup.web.controller;

import com.briup.bean.Shop;
import com.briup.bean.User;
import com.briup.service.IShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@Slf4j
public class ShopController {
    @Autowired
    private IShopService shopService;
    @GetMapping("toViewShop")
    public String toViewShop(Long shopId, Model model, HttpSession session){
        User user= (User) session.getAttribute("user");
        log.info("toViewShop",user.getId(),shopId,new Date().getTime());
        Shop shop=shopService.findShopById(shopId);
        model.addAttribute("shop",shop);
        return "viewShop";
    }
}
