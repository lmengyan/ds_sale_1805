package com.mr.service;

import com.mr.mapper.AttrMapper;
import com.mr.model.OBJECTTMallAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lmy on 2018/11/6.
 */
@Service
public class AttrServiceImpl implements AttrService{

    @Autowired
    private AttrMapper attrMapper;

    public List<OBJECTTMallAttr> fondAttrByclass2(Integer flbh2) {
        return attrMapper.fondAttrByclass2(flbh2);
    }
}
