package com.taotao.content.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.TaoResult;
import com.taotao.content.redis.RedisUtils;
import com.taotao.content.service.ContentService;
import com.taotao.manager.domain.Content;
import com.taotao.manager.mapper.ContentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private RedisUtils redisUtils;

    @Value("${TAOTAO_PORTAL_AD}")
    private String TAOTAO_PORTAL_AD;

    @Override
    public TaoResult<Content> queryContentPage(Integer page, Integer rows, Long categoryId) {
        //查询条件
        Content param = new Content();
        param.setCategoryId(categoryId);

        //查询分页数据
        PageHelper.startPage(page, rows);
        List<Content> list = super.findListByWhere(param);

        PageInfo<Content> info = new PageInfo<>(list);

        //创建返回对象
        TaoResult<Content> result = new TaoResult<>();
        result.setTotal(info.getTotal());
        result.setRows(new ArrayList<>(list));

        return result;
    }

    /**
     * 根据大广告类别id查询广告数据
     * @param taotao_ad_id
     * @return
     */
    @Override
    public String findAD(Long taotao_ad_id) {

        //查询缓存
        String ad = redisUtils.get(TAOTAO_PORTAL_AD + taotao_ad_id);
        if(StringUtils.isNotBlank(ad)) {
            System.out.println("from cache.");
            return ad;
        }

        //查询
        Content param = new Content();
        param.setCategoryId(taotao_ad_id);
        List<Content> list = super.findListByWhere(param);

        //构建一个List集合
        List<Map<String, Object>> results = new ArrayList<>();
        for(Content content : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("srcB", content.getPic());
            map.put("height", 240);
            map.put("alt", "");
            map.put("width", 670);
            map.put("src", content.getPic());
            map.put("widthB", 550);
            map.put("href", content.getUrl());
            map.put("heightB", 240);
            results.add(map);
        }

        String json = "";

        try {
            json = OM.writeValueAsString(results);
            //存入缓存
            redisUtils.set(TAOTAO_PORTAL_AD + taotao_ad_id, json, 60 * 60 * 24);
            System.out.println("from database.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
