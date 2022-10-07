package com.endeavor.take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.endeavor.take_out.dto.DishDto;
import com.endeavor.take_out.entity.Dish;

/**
 * @author Sunrise
 * @create 2022-10-01 11:57
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入才屏的口味数据，西药操作两张表：dish、dish_flavor

    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品和对应的口味信息

    DishDto getByIdWithFlavor(Long id);

    //更新菜品和口味信息

    void updateWithFlavor(DishDto dishDto);
}
