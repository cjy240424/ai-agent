package com.cjy.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjy.domain.po.Prompt;
import lombok.Data;

@Data
public class PageQuery {
    private Integer pageNo = 1; //页码
    private Integer pageSize = 5; //每页大小
    private String sortBy = "update_time"; //排序字段，默认按照更新时间
    private Boolean isAsc = false; //是否升序，默认升序

    public <T> Page<T> toMpPage(Boolean isAsc, String ...sortBys) {
        //1. 构建分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        //2. 构建排序条件
        //因为我的OrderItem()传不进参数，所以只能使用OrderItem.asc/desc()方法
        //2.1 如果有多个排序字段，使用 sortBys 数组
        if (sortBys != null && sortBys.length > 0) {
            for (String field : sortBys) {
                //还想写一个field不能为空字符串的判断
                if (isAsc) {
                    page.addOrder(OrderItem.asc(field));
                } else {
                    page.addOrder(OrderItem.desc(field));
                }
            }
        } else {
            //2.2 如果只有一个排序字段，使用 sortBy
            if (isAsc) {
                page.addOrder(OrderItem.asc(sortBy));
            } else {
                page.addOrder(OrderItem.desc(sortBy));
            }
        }
        return page;
    }
}
