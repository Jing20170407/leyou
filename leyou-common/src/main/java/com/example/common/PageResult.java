package com.example.common;

import java.util.List;

public class PageResult<T> {
    private List<T> items;
    private Long total;         //总条数
    private Integer totalPage;  //总页数
    private Integer curPage;    //当前页
    private Integer pageSize;   //一页大小

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public PageResult(List<T> items, Long total) {
        this.items = items;
        this.total = total;
    }

    public PageResult(List<T> items, Long total, Integer totalPage, Integer curPage) {
        this.items = items;
        this.total = total;
        this.totalPage = totalPage;
        this.curPage = curPage;
    }

    public PageResult() {
    }
}
