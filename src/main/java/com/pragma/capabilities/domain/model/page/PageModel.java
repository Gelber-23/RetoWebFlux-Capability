package com.pragma.capabilities.domain.model.page;

import java.util.List;

public class PageModel <T> {
    private final List<T> content;
    private final long totalElements;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;

    public PageModel(List<T> content, long totalElements, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }

    public List<T> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getPageNumber() { return pageNumber; }
    public int getPageSize() { return pageSize; }
    public int getTotalPages() { return totalPages; }
}