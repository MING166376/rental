package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.service.FlowIndexService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 流量指标控制器
 */
@RestController
@RequestMapping("/flow-index")
public class FlowIndexController {

    @Resource
    private FlowIndexService flowIndexService;

    /**
     * 新增流量指标信息
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody FlowIndex flowIndex) {
        return flowIndexService.saveEntity(flowIndex);
    }

    /**
     * 删除流量指标信息
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public Result<String> delete(@PathVariable Integer id) {
        flowIndexService.removeById(id);
        return ApiResult.success("流量指标删除成功");
    }

    /**
     * 查询流量指标信息
     *
     * @param flowIndexQueryDto 查询参数
     * @return Result<List < FlowIndex>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<FlowIndex>> list(@RequestBody FlowIndexQueryDto flowIndexQueryDto) {
        return flowIndexService.list(flowIndexQueryDto);
    }

}