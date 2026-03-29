package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.FlowIndexMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.em.FlowIndexEnum;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.service.FlowIndexService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 流量指标业务逻辑接口实现类
 */
@Service
public class FlowIndexServiceImpl extends ServiceImpl<FlowIndexMapper, FlowIndex> implements FlowIndexService {


    /**
     * 查询流量指标列表
     *
     * @param flowIndexQueryDto 查询条件
     * @return Result<List < FlowIndex>>
     */
    @Override
    public Result<List<FlowIndex>> list(FlowIndexQueryDto flowIndexQueryDto) {
        List<FlowIndex> flowIndexList = this.baseMapper.list(flowIndexQueryDto);
        Integer count = this.baseMapper.listCount(flowIndexQueryDto);
        return ApiResult.success(flowIndexList, count);
    }

    /**
     * 流量指标信新增
     *
     * @param flowIndex 实体
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(FlowIndex flowIndex) {
        AssertUtils.notNull(flowIndex, "实体数据不能为空");
        AssertUtils.notNull(flowIndex.getType(), "请指定流量类型");
        AssertUtils.hasText(flowIndex.getContentType(), "请指定内容模块");
        AssertUtils.notNull(flowIndex.getContentId(), "请指定内容ID");
        if (Objects.equals(flowIndex.getType(), FlowIndexEnum.REMAIN.getType())) {
            AssertUtils.notNull(flowIndex.getTimes(), "停留时长不能为空");
        }
        flowIndex.setUserId(LocalThreadHolder.getUserId()); // 设置上用户ID
        flowIndex.setCreateTime(LocalDateTime.now()); // 设置时间
        save(flowIndex);
        return ApiResult.success();
    }
}