package com.springboot.mapper;

import com.springboot.entity.ActiveUsersData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhl
 * @since 2024-05-15
 */
@Mapper
public interface ActiveUsersDataMapper extends BaseMapper<ActiveUsersData> {
    @Select("SELECT * FROM active_users_data WHERE date BETWEEN #{startDate} AND #{endDate}")
    List<Map<String, Object>> getAUData(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
