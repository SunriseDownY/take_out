package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.common.CustomException;
import com.endeavor.take_out.entity.Category;
import com.endeavor.take_out.entity.Dish;
import com.endeavor.take_out.entity.Setmeal;
import com.endeavor.take_out.mapper.CategoryMapper;
import com.endeavor.take_out.service.CategoryService;
import com.endeavor.take_out.service.DishService;
import com.endeavor.take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sunrise
 * @create 2022-09-29 11:12
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果关联，抛出一个业务异常
        if (count > 0) {
            //已关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能被删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0) {
            //已关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能被删除");
        }
        super.removeById(id);
    }
}
