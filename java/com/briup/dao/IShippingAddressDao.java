package com.briup.dao;


import com.briup.bean.ShippingAddress;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IShippingAddressDao{
    List<ShippingAddress> findByUserId(Long userId);
    void saveShippingAddress(ShippingAddress shippingAddress);
}
