package com.mr.service;

import com.mr.model.TMallShoppingCar;

import java.util.List;
import java.util.Map;

/**
 * Created by Lmy on 2018/11/8.
 */
public interface CartService {
    List<TMallShoppingCar> listCartByUserId(Integer id);

    void saveCart(TMallShoppingCar cart);

    void updateCartBySkuIdAndUserId(Map<String, Object> cartMap);

    void saveCartList(List<TMallShoppingCar> carList, Integer id);

    TMallShoppingCar findCartBySkuIdAngUserId(Integer skuId, Integer id);

    void updateCartShfxzBySkuIdAndUserId(Integer skuId, Integer id, String shfxz);
}
