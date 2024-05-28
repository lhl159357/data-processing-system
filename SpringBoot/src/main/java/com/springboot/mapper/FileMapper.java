package com.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.Files;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhl
 * @since 2024-03-16
 */
@Mapper
public interface FileMapper extends BaseMapper<Files> {

}
