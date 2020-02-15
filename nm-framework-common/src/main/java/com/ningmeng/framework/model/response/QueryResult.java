package com.ningmeng.framework.model.response;

import lombok.ToString;

import java.util.List;



@ToString
public class QueryResult<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private long total;

    public List<T> getList() {
        return list;
    }

    public long getTotal() {
        return total;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
