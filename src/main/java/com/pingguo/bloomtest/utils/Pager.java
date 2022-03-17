package com.pingguo.bloomtest.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pager<T> {
    private Object list;
    // 当前页码
    private int current;
    // 每页个数
    private int pageSize;
    // 总条数
    private long total;
    // 总页数
    private int totalPages;
}
