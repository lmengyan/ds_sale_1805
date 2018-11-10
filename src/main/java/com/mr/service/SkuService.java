package com.mr.service;

import com.mr.model.OBJECTSku;
import com.mr.model.TMallSku;
import com.mr.model.TMallSkuAttrValueVO;
import com.mr.model.TMallSkuItemVO;

import java.util.List;

/**
 * Created by Lmy on 2018/11/6.
 */
public interface SkuService {
    List<OBJECTSku> listSkuFlbh2(Integer flbh2);

    List<OBJECTSku> listSkuByAttrAndClass2(TMallSkuAttrValueVO attrValueVO, Integer flbh2);

    List<TMallSku> listskuBySpuId(Integer spuId);

    TMallSkuItemVO listItemBySkuId(Integer skuId);
}
