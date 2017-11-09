package com.taotao.manager.service.impl;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.taotao.manager.domain.BasePojo;
import com.taotao.manager.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
public class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {

    public BaseServiceImpl() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType)genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        clazz = (Class<T>)actualTypeArguments[0];
    }

    private Class<T> clazz;

    @Autowired
    private Mapper<T> mapper;

    @Override
    public T findById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.select(null);
    }

    @Override
    public Integer findCountByWhere(T t) {
        return mapper.selectCount(t);
    }

    @Override
    public List<T> findListByWhere(T t) {
        return mapper.select(t);
    }

    @Override
    public List<T> findByPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<T> list = mapper.select(null);
        return list;
    }

    @Override
    public T findOne(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public void save(T t) {
        if(t.getCreated() == null) {
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        } else if(t.getUpdated() == null) {
            t.setUpdated(t.getCreated());
        }
        mapper.insert(t);
    }

    @Override
    public void saveSelective(T t) {
        if(t.getUpdated() == null) {
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        } else if(t.getUpdated() == null) {
            t.setUpdated(t.getCreated());
        }
        mapper.insertSelective(t);
    }

    @Override
    public void updateById(T t) {
        t.setUpdated(new Date());
        mapper.updateByPrimaryKey(t);
    }

    @Override
    public void updateByIdSelective(T t) {
        t.setUpdated(new Date());
        mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(List<Object> ids) {
        Example example = new Example(clazz);
        example.createCriteria().andIn("id", ids);
        mapper.deleteByExample(example);
    }
}
