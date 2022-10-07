package com.endeavor.take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.endeavor.take_out.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sunrise
 * @create 2022-10-04 17:21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
