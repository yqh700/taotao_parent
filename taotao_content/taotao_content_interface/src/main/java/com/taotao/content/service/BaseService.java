package com.taotao.content.service;

import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
public interface BaseService<T> {

    /**
     * 根据 id 查询.
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 查询所有.
     * @return
     */
    List<T> findAll();

    /**
     * 根据条件查询符合条件的记录数.
     * @param t
     * @return
     */
    Integer findCountByWhere(T t);

    /**
     * 根据条件查询符合条件的记录.
     * @param t
     * @return
     */
    List<T> findListByWhere(T t);

    /**
     * 分页查询.
     * @param page 第几页
     * @param rows 每页显示几条
     * @return
     */
    List<T> findByPage(Integer page, Integer rows);

    /**
     * 根据条件查询一条记录.
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * 保存，不忽略 null 值.
     * @param t
     */
    void save(T t);

    /**
     * 保存，忽略 null 值.
     * @param t
     */
    void saveSelective(T t);

    /**
     * 根据 id 更新记录，不忽略 null 值.
     * @param t
     */
    void updateById(T t);

    /**
     * 根据 id 更新记录，忽略 null 值.
     * @param t
     */
    void updateByIdSelective(T t);

    /**
     * 根据 id 删除一条记录.
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据多个 id 删除多条记录.
     * @param ids
     */
    void deleteByIds(List<Object> ids);
}
