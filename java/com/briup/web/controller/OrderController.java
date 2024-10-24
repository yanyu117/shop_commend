package com.briup.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.briup.bean.*;
import com.briup.config.AliPayConfig;
import com.briup.service.IOrderService;
import com.briup.service.IShopCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@Slf4j
public class OrderController {
    @Autowired
    private IShopCarService shopCarService;
    @Autowired
    private IOrderService orderService;
    @GetMapping("/advanceOrder")
    public String advanceOrder(Long[] ids, Model model, HttpSession session){
        List<ShopCar> shopCars=shopCarService.findShopCars(ids);
        model.addAttribute("sumPrice",getSumPrice(shopCars));
        model.addAttribute("shopCarList",shopCars);
        User user= (User) session.getAttribute("user");
        List<ShippingAddress> address=user.getAddresses();
        if(address!=null){
            address.sort((x, y) -> (y.isDefaultValue() ? 1 : 0) - (x.isDefaultValue() ? 1 : 0));
        }
        model.addAttribute("userAddressList",address);
        return "Confirm";
    }

    private BigDecimal getSumPrice(List<ShopCar> shopCars) {
        BigDecimal sumPrice = new BigDecimal("0");
        for (ShopCar shopCar : shopCars) {
            Shop shop = shopCar.getShop();
            if (shop.isDiscount()) {
                sumPrice = sumPrice.add(shop.getDiscountPrice().multiply(new BigDecimal(shopCar.getNum())));
            } else {
                sumPrice = sumPrice.add(shop.getSelling_price().multiply(new BigDecimal(shopCar.getNum())));
            }
        }
        return sumPrice;
    }
    @GetMapping("/createOrder")
    public void createOrder(Long addressId, Long[] ids, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user= (User) session.getAttribute("user");
        Order order=orderService.saveOrder(ids,user,addressId);
        request.setAttribute("order",order);
        request.getRequestDispatcher("payOrder").forward(request, response);
    }
    @GetMapping("/payOrder")
    @ResponseBody
    public void payOrder(HttpServletRequest request, HttpServletResponse response) {

        Order order = (Order) request.getAttribute("order");
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/paySuccess?id="+order.getId();
        AlipayClient alipayClient = AliPayConfig.getAlipayClient();
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        //订单支付
        model.setBody("商品描述：");
        //订单名字
        model.setSubject("杰普购物订单");
        //订单号
        model.setOutTradeNo(order.getId());
        //过期时间
        model.setTimeoutExpress("30m");
        //订单金额
        model.setTotalAmount(order.getSumPrice().toString());
        //产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        //设置参数
        alipayTradePagePayRequest.setBizModel(model);
        //回调地址
        try {
            alipayTradePagePayRequest.setReturnUrl(path);
            //客户端执行,拿到支付结果
            AlipayTradePagePayResponse pageExecute = alipayClient.pageExecute(alipayTradePagePayRequest);
            String result = pageExecute.getBody();
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(result);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/paySuccess")
    public String paySuccess(@RequestParam(name = "id") String orderId, Model model, HttpSession session){
        Order order = orderService.paySuccess(orderId);
        model.addAttribute("order",order);
        return "confirmSuc";
    }

}
