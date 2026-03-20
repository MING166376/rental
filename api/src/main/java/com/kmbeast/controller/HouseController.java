package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HouseQueryDto;
import com.kmbeast.pojo.entity.House;
import com.kmbeast.pojo.vo.HouseListItemVO;
import com.kmbeast.pojo.vo.SelectedVO;
import com.kmbeast.service.HouseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 房屋控制器
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Resource
    private HouseService houseService;

    /**
     * 新增房屋信息
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody House house) {
        return houseService.saveEntity(house);
    }

    /**
     * 删除房屋信息
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public Result<String> delete(@PathVariable Integer id) {
        houseService.removeById(id);
        return ApiResult.success("房屋删除成功");
    }

    /**
     * 修改房屋信息
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody House house) {
        return houseService.update(house);
    }

    /**
     * 查询房屋类型列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/houseTypeList")
    @ResponseBody
    public Result<List<SelectedVO>> houseTypeList() {
        return houseService.houseTypeList();
    }

    /**
     * 查询房屋朝向列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/houseDirectionList")
    @ResponseBody
    public Result<List<SelectedVO>> houseDirectionList() {
        return houseService.houseDirectionList();
    }

    /**
     * 查询房屋户型列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/houseSizedList")
    @ResponseBody
    public Result<List<SelectedVO>> houseSizedList() {
        return houseService.houseSizedList();
    }

    /**
     * 查询房屋押金方式列表
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @GetMapping(value = "/houseDepositMethodList")
    @ResponseBody
    public Result<List<SelectedVO>> houseDepositMethodList() {
        return houseService.houseDepositMethodList();
    }

    /**
     * 查询房屋信息
     *
     * @param houseQueryDto 查询参数
     * @return Result<List < HouseListItemVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<HouseListItemVO>> list(@RequestBody HouseQueryDto houseQueryDto) {
        return houseService.list(houseQueryDto);
    }

}