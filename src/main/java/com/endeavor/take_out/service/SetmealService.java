package com.endeavor.take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.endeavor.take_out.dto.SetmealDto;
import com.endeavor.take_out.entity.Setmeal;

import java.util.List;

/**
 * @author Sunrise
 * @create 2022-10-01 11:58
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    void updateWithStatus(Integer status, List<Long> ids);

    SetmealDto getByIdWithDto(Long id);

    void updateWithDto(SetmealDto setmealDto);
}

