package com.taotao.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/8.
 */
public class TaoResult<T> implements Serializable {

    private List<T> rows;

    private Long total;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
