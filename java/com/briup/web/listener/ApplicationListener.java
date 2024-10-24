package com.briup.web.listener;

import com.briup.bean.Shop;
import com.briup.bean.vo.CategoryVO;
import com.briup.service.ICategoryService;
import com.briup.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

@Component
public class ApplicationListener implements ServletContextListener {
    @Autowired
    private IShopService shopService;
    @Autowired
    private ICategoryService categoryService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<Shop> shops=shopService.findAllShops();
        List<CategoryVO> categoey=categoryService.findAllCategoey();
        List<Shop> discount=shopService.findDiscount();
        ServletContext sc=sce.getServletContext();
        sc.setAttribute("shops",shops);
        sc.setAttribute("categories",categoey);
        sc.setAttribute("discountList",discount);

    }
}
