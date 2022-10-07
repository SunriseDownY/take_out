package com.endeavor.take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.endeavor.take_out.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sunrise
 * @create 2022-09-29 11:05
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
