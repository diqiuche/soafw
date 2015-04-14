package com.kjt.service.common.result;


import java.util.List;

/**
 * 用于分页的工具类
 * 
 * @author
 */
public class Pager<T extends IResult> implements IResult {

    /**
     * 
     */
    private static final long serialVersionUID = -6706512763401599836L;

    private int total = 0; // 总记录数
    private int pageSize = 10; // 每页显示记录数
    private int pageCount = 0; // 总页数
    private int offset = 1; // 当前页,默认查询时为 1

    private boolean isFirstPage = false; // 是否为第一页
    private boolean isLastPage = false; // 是否为最后一页
    private boolean hasPreviousPage = false; // 是否有前一页
    private boolean hasNextPage = false; // 是否有下一页

    private List<T> list;

    public static void main(String[] args) {
        Pager pager = new Pager(56, 1, 10);
        System.out.println(pager);
    }

    public Pager(int total, int offset) {
        init(total, offset, pageSize);
    }

    public Pager(int total, int offset, int pageSize) {
        init(total, offset, pageSize);
    }

    private void init(int total, int offset, int pageSize) {
        // 设置基本参数
        this.total = total;
        this.pageSize = pageSize;
        this.pageCount = (this.total - 1) / this.pageSize + 1;

        // 根据输入可能错误的当前号码进行自动纠正
        if (offset < 0) {
            this.offset = 0;
        } else if (offset > this.pageCount) {
            this.offset = this.pageCount;
        } else {
            this.offset = offset;
        }

        // 以及页面边界的判定
        judgePageBoudary();
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = offset == 0;
        isLastPage = offset == pageCount && offset != 0;
        hasPreviousPage = offset >= 1;
        hasNextPage = offset < pageCount;
    }

    public int getTotal() {
        return total;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }



}
