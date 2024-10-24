package com.briup.service.impl;

import com.briup.bean.*;
import com.briup.dao.IOrderDao;
import com.briup.dao.IOrderItemDao;
import com.briup.dao.IShopCarDao;
import com.briup.dao.IShopDao;
import com.briup.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IOrderServiceImpl implements IOrderService {
    @Autowired
    private IShopCarDao shopCarDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderItemDao itemDao;

    @Override
    public List<Order> findUserAllOrders(Long userId) {
        return null;
    }

    @Override
    public Order saveOrder(Long[] shopCarIds, User user, Long addressId) {
        ShippingAddress shippingAddress=new ShippingAddress(addressId);
        List<Long> list= Arrays.asList(shopCarIds);
        List<ShopCar> shopcars=shopCarDao.findShopCarByIds(list);
        Order order=new Order(user,shippingAddress);
        List<OrderItem> items = shopcars.stream().map(i -> {
            OrderItem item = new OrderItem(i);
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());
        order.setOrderItems(items);
        order.setStatus(1);
        orderDao.saveOrder(order);
        items.forEach(x->{
            itemDao.saveOrderItem(x);
        });
        shopCarDao.deleteShopCarByIds(list);
        return order;
    }

    @Override
    public Order paySuccess(String orderId) {
        Order order=orderDao.findOrderByOrderId(orderId);
        order.setStatus(2);
        orderDao.updateOrder(order);
        return order;
    }

    @Override
    public Order findById(String orderId) {
        return null;
    }
}
