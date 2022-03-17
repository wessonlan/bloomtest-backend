package com.pingguo.bloomtest.utils;

import com.github.pagehelper.PageInfo;

public class PageUtils {
    public static <T> Pager<T> setPageInfo(PageInfo pageInfo, T obj) {
        Pager<T> pager = new Pager<>();
        pager.setList(obj);
        pager.setCurrent(pageInfo.getPageNum());
        pager.setPageSize(pageInfo.getPageSize());
        pager.setTotal(pageInfo.getTotal());
        pager.setTotalPages(pageInfo.getPages());
        System.out.println(pageInfo.getPages());
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPageNum());
        System.out.println(pageInfo.getPageSize());
        System.out.println(pageInfo.getSize());
        return pager;
    }
}
