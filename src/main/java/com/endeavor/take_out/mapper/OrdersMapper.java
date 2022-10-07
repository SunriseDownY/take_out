package com.endeavor.take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.endeavor.take_out.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sunrise
 * @create 2022-10-06 17:33
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
