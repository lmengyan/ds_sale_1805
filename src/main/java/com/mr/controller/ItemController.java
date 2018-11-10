package com.mr.controller;

import com.mr.model.TMallSku;
import com.mr.model.TMallSkuItemVO;
import com.mr.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Lmy on 2018/11/7.
 */
@Controller
public class ItemController {

    @Autowired
    private SkuService skuService;
    /**
     * 跳转商品详情页面
     * @param skuId
     * @param map
     * @return
     */
    @RequestMapping("toItmePage")
    public String toItmePage(Integer skuId ,Integer spu, ModelMap map){

        //sku数据
        TMallSkuItemVO itemvo=skuService.listItemBySkuId(skuId);
        //spuid查询到的sku的集合
        List<TMallSku> skuList = skuService.listskuBySpuId(spu);// 传递spuId返回集合

        map.put("itemvo",itemvo);
        map.put("skuList",skuList);
        return  "item";
    }
}
