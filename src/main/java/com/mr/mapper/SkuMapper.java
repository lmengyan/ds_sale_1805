package com.mr.mapper;

import com.mr.model.OBJECTSku;
import com.mr.model.TMallSku;
import com.mr.model.TMallSkuItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Lmy on 2018/11/6.
 */
public interface SkuMapper {
    List<OBJECTSku> listSkuFlbh2(@Param("flbh2") Integer flbh2);

    List<OBJECTSku> listSkuByAttrAndClass2(Map<String, Object> map);

    List<TMallSku> listskuBySpuId(@Param("spuId") Integer spuId);

    TMallSkuItemVO listItemBySkuId(@Param("skuId") Integer skuId);
}
