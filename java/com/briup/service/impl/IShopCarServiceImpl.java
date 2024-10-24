package com.briup.service.impl;

import com.briup.bean.ShopCar;
import com.briup.dao.IShopCarDao;
import com.briup.service.IShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class IShopCarServiceImpl implements IShopCarService {
    @Autowired
    private IShopCarDao shopCarDao;

    @Override
    public List<ShopCar> findUserAllShopCar(Long userId) {
        return shopCarDao.findByUserId(userId);
    }

    @Override
    public void saveShopCar(int num, Long userId, Long shopId) {
        ShopCar cart=shopCarDao.findByShopIdAndUserId(shopId,userId);
        if(cart==null){
            shopCarDao.saveByshopIdUserId(num,shopId,userId);
        }else{
            shopCarDao.updateShopcar(cart.getNum()+1,cart.getId());
        }

    }

    @Override
    public void deleteShopCar(Long id) {

    }

    @Override
    public void updateShopCar(Long id, int num) {

    }

    @Override
    public List<ShopCar> findShopCars(Long[] ids) {
        List<Long> list= Arrays.asList(ids);
        return shopCarDao.findShopCarByIds(list);
    }
}
