package com.briup.dao;

import com.briup.bean.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IShopDao{
    void updateSalesVolume(Long shopId,Long salesVolume);
    void updateVisitVolume(Long shopId,Long visitVolume);
    List<Shop> findByDiscountOrderBySalesVolumeDesc(boolean discount);
    List<Shop> findByCategoryId(Long categoryId);
    List<Shop> findByNameContaining(String shopName);
    List<Shop> findAllShopByVisitVolumeDesc();
    Shop findShopById(long id);
}
