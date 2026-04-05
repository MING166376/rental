package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.HouseOrderInfoQueryDto;
import com.kmbeast.pojo.entity.HouseOrderInfo;
import com.kmbeast.pojo.vo.HouseOrderInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 预约看房持久化接口
 */
@Mapper
public interface HouseOrderInfoMapper extends BaseMapper<HouseOrderInfo> {

    List<HouseOrderInfoVO> list(HouseOrderInfoQueryDto houseOrderInfoQueryDto);

    Integer listCount(HouseOrderInfoQueryDto houseOrderInfoQueryDto);

}