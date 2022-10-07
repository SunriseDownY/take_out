package com.endeavor.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endeavor.take_out.common.CustomException;
import com.endeavor.take_out.dto.SetmealDto;
import com.endeavor.take_out.entity.Setmeal;
import com.endeavor.take_out.entity.SetmealDish;
import com.endeavor.take_out.mapper.SetmealMapper;
import com.endeavor.take_out.service.SetmealDishService;
import com.endeavor.take_out.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sunrise
 * @create 2022-10-01 11:59
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，并保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) throw new CustomException("套餐正在被售卖中，不能删除");
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        //删除关系表中的数据-setmeal_dish
        setmealDishService.remove(queryWrapper1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithStatus(Integer status, List<Long> ids) {
        List<Setmeal> setmeals = ids.stream().map((item) -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(item);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());
        this.updateBatchById(setmeals);
    }

    @Override
    public SetmealDto getByIdWithDto(Long id) {
        Setmeal setmeal = this.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getId() != null, SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithDto(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDto.getId() != null, SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
