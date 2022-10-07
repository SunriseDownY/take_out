package com.endeavor.take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.endeavor.take_out.common.R;
import com.endeavor.take_out.dto.DishDto;
import com.endeavor.take_out.entity.Category;
import com.endeavor.take_out.entity.Dish;
import com.endeavor.take_out.entity.DishFlavor;
import com.endeavor.take_out.service.CategoryService;
import com.endeavor.take_out.service.DishFlavorService;
import com.endeavor.take_out.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sunrise
 * @create 2022-10-01 22:34
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //将遍历出来的item拷贝到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //有菜品，判断有无分类
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                //有分类id，判断是否有分类
                if (category != null)
                    dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     *
     * @param dish
     * @return
     */
    //@GetMapping("/list")
    //public R<List<Dish>> list(Dish dish) {
    //    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
    //    //添加条件，查询状态为1（起售状态）的菜品
    //    queryWrapper.eq(Dish::getStatus, 1);
    //    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //    List<Dish> list = dishService.list(queryWrapper);
    //    return R.success(list);
    //}
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(queryWrapper);

        List<DishDto> dishDtos = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //将遍历出来的item拷贝到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //有菜品，判断有无分类
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                //有分类id，判断是否有分类
                if (category != null)
                    dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(item.getId() != null, DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }
}
