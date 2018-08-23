package com.qnyy.re.base.util.container;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//分页查询结果对象
@Getter
public class PageResult<T> {
    public static final Integer MAX_SIZE = Integer.MAX_VALUE;
    protected List<T> result;// 当前页的结果集数据:查询
    protected Integer totalCount;// 总数据条数:查询

    protected Integer currentPage = 1;
    protected Integer pageSize = 10;

    protected Integer prevPage;// 上一页
    protected Integer nextPage;// 下一页
    protected Integer totalPage;// 总页数

    public int getTotalPage() {
        return totalPage == 0 ? 1 : totalPage;
    }

    public PageResult(List<T> result, Integer totalCount, Integer currentPage, Integer pageSize) {
        if (totalCount > 0) {
            this.result = result;
            this.totalCount = totalCount;
            this.currentPage = currentPage;
        } else {
            // 如果总数据条数为0,返回一个空集
            this.result = new ArrayList<>();
            this.totalCount = 0;
            this.currentPage = 1;
        }
        this.pageSize = pageSize;
        // ----------------------------------------
        this.totalPage = this.totalCount % this.pageSize == 0 ? this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1;
        this.totalPage = this.totalPage == 0 ? 1 : this.totalPage;
        this.prevPage = this.currentPage - 1 >= 1 ? this.currentPage - 1 : 1;
        this.nextPage = this.currentPage + 1 <= this.totalPage ? this.currentPage + 1 : this.totalPage;
    }
}
