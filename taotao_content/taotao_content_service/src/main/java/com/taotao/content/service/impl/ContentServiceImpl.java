package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.TaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.manager.domain.Content;
import com.taotao.manager.mapper.ContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {

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
}
