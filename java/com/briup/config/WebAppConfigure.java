package com.briup.config;

import com.briup.web.filter.UserFilter;
import com.briup.web.listener.ApplicationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebAppConfigure implements WebMvcConfigurer {
    @Autowired
    private UserFilter userFilter;
    @Autowired
    private ApplicationListener applicationListener;
    @Bean
    public FilterRegistrationBean filterRegist() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(userFilter);
        frBean.addUrlPatterns("/toShopCar","/toOrder","/toShopView","/addShopCar","/toCollect","/advanceOrder","/toViewShop","/createOrder","/payOrder","/paySuccess","/toAddShippingAddress","/addShippingAddress");
        return frBean;
    }


    @Bean
    public ServletListenerRegistrationBean listenerRegist() {
        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
        srb.setListener(applicationListener);
        return srb;
    }
}