<template>
  <div class="container">
    <div class="top-healder">
      <div class="nav-left">
        <Tab :buttons="[
                    { label: '全部', value: 'null' },
                    { label: '未审核', value: '0' },
                    { label: '已审核', value: '1' }
                ]" initialActive="null" @change="handleChange" />
      </div>

      <div class="nav-right">
        <div>
          <AutoInput placeholder="搜索身份证号" @listener="listener" />
        </div>

      </div>

    </div>

    <div>
      <!-- 房源信息渲染区域 -->
      <div class="house-container">
        <div class="house-item" v-for="item in apiResult.data" :key="item.id">
          <div>
            <img :src="item.cover" alt="" srcset="">
          </div>

          <div>
            <div class="name">
              {{ item.name }}
            </div>

            <div class="point">
              <!-- 指标信息区域 -->
              <div class="left">
                <div>
                  <i class="el-icon-location-information"></i>

                  {{ item.cityAreaName }} · {{ item.communityName }}
                </div>

                <div>
                  {{ item.sizeNumber }}㎡
                </div>

                <div>
                  {{ item.directionName }}
                </div>

                <div>
                  {{ item.fitmentStatusName }}
                </div>

                <div>
                  {{ item.depositMethodName }}
                </div>

              </div>

              <!-- 功能操作区 -->
              <div class="right">
                <div><i class="el-icon-edit"></i>编辑</div>

                <div><i class="el-icon-delete"></i>删除</div>

              </div>

            </div>

            <div class="rent">
              ￥{{ item.rent }}
            </div>

          </div>

        </div>

      </div>

      <!-- 分页组件区域 -->
      <div class="pager">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="houseQueryDto.current" :page-sizes="[10, 20]" :page-size="houseQueryDto.size"
                       layout="total, sizes, prev, pager, next, jumper" :total="apiResult.total"></el-pagination>

      </div>

    </div>

    <!-- 删除确认弹窗 -->
    <el-dialog title="删除房源信息" :show-close="false" :visible.sync="dialogDeletedVisible" width="20%">
      <span>确定删除房源信息数据？</span>

      <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="dialogDeletedVisible = false">取消</el-button>

                <el-button size="mini" type="primary" @click="confirmDeleted">确定</el-button>

            </span>

    </el-dialog>

    <!-- 房源申请信息抽屉 -->
    <el-drawer title="房源申请信息" :modal="false" :wrapperClosable="false" :visible.sync="drawer" :direction="direction"
               size="70%" :before-close="handleClose">
      <div style="gap: 30px;display: flex;justify-content: center;align-items: center;">
        <div>
          <img style="width: 80px;height: 80px;border-radius: 50%;" :src="landlord.avatar" alt="" srcset="">
          <div style="text-align: center;font-size: 24px;">{{ landlord.username }}</div>

        </div>

        <div
            style="width: 600px;background-color: rgb(250,250,250);padding: 20px 60px;box-shadow: 0 4px 8px rgb(240,240,240);border-radius: 5px;">
          <div>
            <p>*身份证号</p>

            <div style="font-size: 18px;">{{ landlord.idcard }}</div>

          </div>

          <div style="display: flex;">
            <div>
              <p>*身份证正面照</p>

              <img style="width: 200px;height: 140px;" :src="landlord.idcardFront" alt="">
            </div>

            <div>
              <p>*身份证反面照</p>

              <img style="width: 200px;height: 140px;" :src="landlord.idcardOpposite" alt="">
            </div>

          </div>

        </div>

      </div>

      <div v-if="!landlord.isAudit" style="text-align: center;margin-top: 30px;">
                <span @click="auditLandlord" class="info-bt"><i class="el-icon-finished"
                                                                style="margin-right: 5px;"></i>通过审核</span>

      </div>

    </el-drawer>

  </div>

</template>

<script>
// B站 「程序辰星」原创出品
import AutoInput from "@/components/AutoInput.vue"; // 自己封装好的输入框组件
import Tab from "@/components/Tab.vue";
export default {
  components: { AutoInput, Tab }, // 注册组件
  data() {
    return {
      drawer: false,
      direction: 'ttb',
      id: null, // 页面即将删除的数据ID
      apiResult: { // 后端返回的查询数据的响应数据
        data: [], // 数据项
        total: 0, // 符合条件的数据总想 - 初始赋值为0
      },
      landlord: {}, // 房源信息
      houseQueryDto: { // 搜索条件
        current: 1, // 当前页 - 初始是第一页
        size: 10, // 页面显示大小 - 初始是10条
      },
      dialogDeletedVisible: false, // 删除弹窗控制开关变量 - 初始是关（false）
    };
  },
  created() {
    this.fetchFreshData();
  },
  methods: {
    handleChange(val) {
      this.houseQueryDto.isAudit = Number(val.value);
      this.fetchFreshData();
    },
    handleClose() {
      this.drawer = false;
    },
    handleDetail(data) {
      this.drawer = true;
      this.landlord = data;
    },
    // 输入框组件输入回传
    listener(text) {
      this.houseQueryDto.idcard = text; // 赋值查询条件的内容
      this.fetchFreshData(); // 重新加载数据
    },
    async auditLandlord() {
      try {
        const landlord = {
          id: this.landlord.id,
          isAudit: true,
        }
        await this.$axios.put('/house/update', landlord);
        this.$message.success('审核成功');
        this.drawer = false; // 关闭详情抽屉
        this.fetchFreshData(); // 重新加载房源数据
      } catch (error) {
        this.$message.info(error.message);
        console.error('审核房源信息信息异常:', error);
      }
    },
    // 查询房源信息数据
    async fetchFreshData() {
      try {
        const { data, total } = await this.$axios.post('/house/landlordHouseList', this.houseQueryDto);
        this.apiResult.data = data;
        this.apiResult.total = total;
      } catch (error) {
        console.error('查询房源信息信息异常:', error);
      }
    },
    // 分页 - 处理页面页数切换
    handleSizeChange(size) {
      this.houseQueryDto.size = size; // 当前页面大小重置
      this.houseQueryDto.currrent = 1; // 当前页设置为第一页
      this.fetchFreshData(); // 重新加载页面数据
    },
    // 分页 - 处理页面当前页切换
    handleCurrentChange(current) {
      this.houseQueryDto.current = current; // 当前页选中
      this.fetchFreshData(); // 重新加载页面数据
    },
    // 表格点击删除房源信息
    handleDelete(row) {
      this.dialogDeletedVisible = true; // 开启删除弹窗确认
      this.id = row.id;
    },
    // 房源信息删除
    async confirmDeleted() {
      try {
        const { code } = await this.$axios.delete(`/house/${this.id}`);
        if (code === 200) {
          this.$notify.success({
            title: '房源信息删除',
            message: '删除成功',
            position: 'buttom-right',
            suration: 1000,
          });
          this.dialogDeletedVisible = false; // 关闭删除确认弹窗
          this.id = null; // 将标识ID置位
          this.fetchFreshData(); // 删除房源信息数据之后，重新加载房源信息数据
        }
      } catch (error) {
        console.log("删除房源信息数据异常：", error);
      }
    }
  },
};
</script>

<style scoped lang="scss">
.house-item {

  display: flex;
  gap: 20px;
  padding-block: 30px;
  border-bottom: 1px solid rgb(240, 240, 240);

  img {
    width: 130px;
    height: 90px;
  }

  .name {
    font-size: 20px;
  }

  .point {
    display: flex;
    margin-block: 10px;
    min-width: 600px;
    justify-content: space-between;
    box-sizing: border-box;

    .right {
      display: flex;
      gap: 20px;

      div {
        font-size: 14px;
        border: 1px solid rgb(193, 193, 193);
        color: rgb(78, 77, 77);
        cursor: pointer;
        padding: 4px 16px;

        &:hover {
          border: 1px solid rgb(139, 174, 236);
          color: rgb(139, 174, 236);
        }
      }
    }

    .left {
      display: flex;
      justify-content: left;
      align-items: center;
      gap: 20px;

      div {
        background-color: rgb(245, 245, 245);
        border-radius: 2px;
        font-size: 12px;
        padding: 2px 4px;
        color: rgb(56, 60, 89);
      }
    }

  }

  .rent {
    font-size: 20px;
    font-weight: 900;
    color: rgb(236, 108, 97);
  }

}


.pager {
  margin-block: 20px;
}

/* 默认隐藏操作按钮 */
.operate-buttons {
  opacity: 0;
  transition: opacity 0.3s;
  /* 添加过渡效果 */
  cursor: pointer;

  i {
    padding: 8px;
    border-radius: 6px;
    transition: all .5s ease;

    &:hover {
      background-color: rgb(236, 237, 238);
    }
  }

}

/* 行悬停时显示操作按钮 */
.el-table__body tr:hover .operate-buttons {
  opacity: 1;
}

.container {
  padding: 20px 30px;
  background-color: rgb(255, 255, 255);
}

.top-healder {
  margin-block: 10px;
  padding-inline: 10px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .nav-left,
  .nav-right {
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 10px;
  }

  .nav-left {
    display: flex;
  }

}
</style>
