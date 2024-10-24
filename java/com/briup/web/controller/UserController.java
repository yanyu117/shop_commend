package com.briup.web.controller;

import com.briup.bean.User;
import com.briup.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    @GetMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/register")
    public String register(User user, Model model){
        try {
            userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg",e.getMessage());
            return "register";
        }
        return "login";
    }
    @PostMapping("/login")
    public String login(String loginName, String password, HttpSession session,Model model){
        try {
            User user=userService.login(loginName,password);
            session.setAttribute("user",user);
            //查询当前用户推荐的商品列表,存入session对象
            //session.setAttribute("recommendShops",);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg",e.getMessage());
            return "login";
        }
        return "index";
    }

}
