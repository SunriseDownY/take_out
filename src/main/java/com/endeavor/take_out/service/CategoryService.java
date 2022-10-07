package com.endeavor.take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.endeavor.take_out.entity.Category;

/**
 * @author Sunrise
 * @create 2022-09-29 11:07
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
