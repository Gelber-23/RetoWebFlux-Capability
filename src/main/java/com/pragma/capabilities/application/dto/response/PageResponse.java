package com.pragma.capabilities.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PageResponse <T>{
    private List<T> content;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
}
