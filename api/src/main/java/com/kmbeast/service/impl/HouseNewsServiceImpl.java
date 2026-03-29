package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.FlowIndexMapper;
import com.kmbeast.mapper.HouseNewsMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.dto.HouseNewsQueryDto;
import com.kmbeast.pojo.em.FlowIndexEnum;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.pojo.entity.HouseNews;
import com.kmbeast.pojo.vo.HouseNewsListVO;
import com.kmbeast.service.FlowIndexService;
import com.kmbeast.service.HouseNewsService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 房屋资讯业务逻辑接口实现类
 */
@Service
public class HouseNewsServiceImpl extends ServiceImpl<HouseNewsMapper, HouseNews> implements HouseNewsService {


    /**
     * 查询房屋资讯列表
     *
     * @param houseNewsQueryDto 查询条件
     * @return Result<List < HouseNewsListVO>>
     */
    @Override
    public Result<List<HouseNewsListVO>> list(HouseNewsQueryDto houseNewsQueryDto) {
        List<HouseNewsListVO> houseNewsListVOList = this.baseMapper.list(houseNewsQueryDto);
        Integer count = this.baseMapper.listCount(houseNewsQueryDto);
        return ApiResult.success(houseNewsListVOList, count);
    }

    /**
     * 房屋资讯新增
     *
     * @param houseNews 实体
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(HouseNews houseNews) {
        judge(houseNews);
        houseNews.setCreateTime(LocalDateTime.now()); // 设置时间
        save(houseNews);
        return ApiResult.success();
    }

    @Override
    public Result<HouseNewsListVO> selectById(Integer id) {
        return null;
    }

    @Override
    public Result<String> updateEntity(HouseNews houseNews) {
        return null;
    }

    private void judge(HouseNews houseNews) {
        AssertUtils.notNull(houseNews, "实体数据不能为空");
        AssertUtils.hasText(houseNews.getTitle(), "标题不能为空");
        AssertUtils.hasText(houseNews.getCover(), "封面不能为空");
        AssertUtils.hasText(houseNews.getSummary(), "摘要不能为空");
    }


}