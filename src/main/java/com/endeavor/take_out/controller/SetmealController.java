package com.endeavor.take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.endeavor.take_out.common.R;
import com.endeavor.take_out.dto.SetmealDto;
import com.endeavor.take_out.entity.Category;
import com.endeavor.take_out.entity.Setmeal;
import com.endeavor.take_out.service.CategoryService;
import com.endeavor.take_out.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sunrise
 * @create 2022-10-03 0:52
 */
@RestController
@RequestMapping("/setmeal")

public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 将修改的内容回显
     *
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable("id") Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDto(id);
        //获取菜品名
        setmealDto.setCategoryName(categoryService.getById(setmealDto.getCategoryId()).getName());
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDto(setmealDto);
        return R.success("套餐修改成功");
    }

    /**
     * 新增套餐
     *
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 批量修改套餐状态
     *
     * @param request
     * @param ids
     * @return
     */
    @PostMapping("/status/*")
    public R<String> update(HttpServletRequest request,
                            @RequestParam List<Long> ids) {
        String url = request.getRequestURL().toString();
        String substring = url.substring(url.lastIndexOf("/") + 1);
        Integer status = new Integer(substring);
        setmealService.updateWithStatus(status, ids);
        return R.success("该套餐售卖状态修改成功");
    }

    /**
     * 套餐列表展示
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    private R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                if (category != null) {
                    setmealDto.setCategoryName(category.getName());
                }
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtos);
        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        return R.success(setmeals);
    }
}
