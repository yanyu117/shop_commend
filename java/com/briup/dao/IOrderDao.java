package com.briup.dao;

import com.briup.bean.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IOrderDao {
	List<Order> findByUserId(Long user_id);
	void saveOrder(Order order);
	Order findOrderByOrderId(String order_id);
	void updateOrder(Order order);
}
