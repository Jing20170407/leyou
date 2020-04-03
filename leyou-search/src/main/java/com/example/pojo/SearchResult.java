package com.example.pojo;

import com.example.common.PageResult;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {
    private List<Map<String,Object>> categoryAggs;
    private List<Brand> brandAggs;
    private List<Map<String,Object>> specAggs;
    private Map<String,Object> filter;

    public SearchResult(List<Goods> items, Long total, List<Map<String, Object>> categoryAggs, List<Brand> brandAggs, List<Map<String, Object>> specAggs, Map<String, Object> filter) {
        super(items, total);
        this.categoryAggs = categoryAggs;
        this.brandAggs = brandAggs;
        this.specAggs = specAggs;
        this.filter = filter;
    }

    public SearchResult(List<Goods> items, Long total, Integer totalPage, Integer curPage, List<Map<String, Object>> categoryAggs, List<Brand> brandAggs, List<Map<String, Object>> specAggs, Map<String, Object> filter) {
        super(items, total, totalPage, curPage);
        this.categoryAggs = categoryAggs;
        this.brandAggs = brandAggs;
        this.specAggs = specAggs;
        this.filter = filter;
    }

    public SearchResult(List<Map<String, Object>> categoryAggs, List<Brand> brandAggs, List<Map<String, Object>> specAggs, Map<String, Object> filter) {
        this.categoryAggs = categoryAggs;
        this.brandAggs = brandAggs;
        this.specAggs = specAggs;
        this.filter = filter;
    }

    public List<Map<String, Object>> getCategoryAggs() {
        return categoryAggs;
    }

    public void setCategoryAggs(List<Map<String, Object>> categoryAggs) {
        this.categoryAggs = categoryAggs;
    }

    public List<Brand> getBrandAggs() {
        return brandAggs;
    }

    public void setBrandAggs(List<Brand> brandAggs) {
        this.brandAggs = brandAggs;
    }

    public List<Map<String, Object>> getSpecAggs() {
        return specAggs;
    }

    public void setSpecAggs(List<Map<String, Object>> specAggs) {
        this.specAggs = specAggs;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }
}
