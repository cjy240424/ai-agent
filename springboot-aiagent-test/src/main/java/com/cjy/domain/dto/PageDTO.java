package com.cjy.domain.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjy.domain.vo.PromptVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import cn.hutool.core.bean.BeanUtil;     // ← Hutool 的 BeanUtil
import cn.hutool.core.collection.CollUtil; // ← Hutool 的 CollUtil


import java.util.Collections;
import java.util.List;

@Data
public class PageDTO<T> {
    private Long total; //总结果数
    private Long pages; //总页数
    private List<T> list; //结果集合

    public static <PO, VO> PageDTO<VO> of(Page<PO> p, Class<VO> clazz) {
        //3. 封装结果
        PageDTO<VO> pageDto = new PageDTO<>();
        //3.1 设置总结果数
        pageDto.setTotal(p.getTotal());
        //3.2 设置总页数
        pageDto.setPages(p.getPages());
        //3.3 封装结果集合
        List<PO> records = p.getRecords();
        if (CollUtil.isEmpty(records)) {
            pageDto.setList(Collections.emptyList());
            return pageDto;
        }
        // 3.4. 拷贝 user 的 VO
        pageDto.setList(BeanUtil.copyToList(records,clazz));
        // 3.5. 返回
        return pageDto;
    }
}
