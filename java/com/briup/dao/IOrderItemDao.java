package com.briup.dao;


import com.briup.bean.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderItemDao{
    void saveOrderItem(OrderItem orderItem);
}
