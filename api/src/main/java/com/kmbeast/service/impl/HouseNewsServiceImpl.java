package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.mapper.HouseNewsMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HouseNewsQueryDto;
import com.kmbeast.pojo.entity.HouseNews;
import com.kmbeast.pojo.vo.HouseNewsListVO;
import com.kmbeast.service.HouseNewsService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 通过ID查询房屋资讯
     *
     * @param id 房屋ID
     * @return Result<HouseNews>
     */
    @Override
    public Result<HouseNews> selectById(Integer id) {
        AssertUtils.notNull(id, "ID不能为空");
        HouseNews houseNews = getById(id);
        return ApiResult.success(houseNews);
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
        AssertUtils.isTrue(houseNews.getTitle().length() < 30, "标题最多30个字");
        AssertUtils.isTrue(houseNews.getSummary().length() < 200, "摘要最多200个字");
    }


}