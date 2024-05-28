package com.springboot.mapper;

import com.springboot.entity.RegisterRecords;
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
 * @since 2024-05-14
 */
@Mapper
public interface RegisterRecordsMapper extends BaseMapper<RegisterRecords> {

    @Select("SELECT registerTime, channels, COUNT(*) AS count FROM register_records WHERE registerTime BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY registerTime, channels " +
            "ORDER BY registerTime, channels")
    List<Map<String, Object>> getOptionData(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
