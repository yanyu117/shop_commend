package com.briup.web.controller;

import com.briup.bean.Shop;
import com.briup.bean.ShopCar;
import com.briup.bean.User;
import com.briup.service.IShopCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class ShopCarController {
    @Autowired
    private IShopCarService shopCarService;

    @GetMapping("/addShopCar")
    @ResponseBody
    public void addShopCar(Long shopId, HttpSession session){
        User user= (User) session.getAttribute("user");
        long userid= user.getId();
        shopCarService.saveShopCar(1,userid,shopId);
    }

    @GetMapping("/toShopCar")
    public String toShopCar(HttpSession session, Model model){
        User user= (User) session.getAttribute("user");
        List<ShopCar> shopcars=shopCarService.findUserAllShopCar(user.getId());
        model.addAttribute("shopCarList",shopcars);
        return "shopCar";
    }
}
