package com.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhl
 * @since 2024-03-31
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
