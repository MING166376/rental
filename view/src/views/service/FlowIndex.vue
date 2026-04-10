<template>
  <div class="container">
    <div class="top-header">
      <div class="nav-left">
      </div>

      <div class="nav-right">
        <div>
          <AutoInput placeholder="搜索房源" @listener="listener" />
        </div>

      </div>

    </div>

    <!-- 表格及分页信息 -->
    <div style="padding-inline: 20px;">
      <el-table :data="apiResult.data">
        <el-table-column prop="username" label="基本信息">
          <template #default="scope">
            <div style="display: flex;justify-content: left;gap: 10px;">
              <img style="border-radius: 5px;width: 70px;height: 50px;" :src="scope.row.houseCover" alt=""
                   srcset="">
              <div>
                <div style="font-size: 18px;">
                  {{ scope.row.houseName }}
                </div>

                <div style="margin-top: 4px;">
                  {{ scope.row.createTime }}
                </div>

              </div>

            </div>

          </template>

        </el-table-column>

        <el-table-column prop="showNumber" :sortable="true" width="108" label="展现量"></el-table-column>

        <el-table-column prop="viewNumber" :sortable="true" width="108" label="阅读量"></el-table-column>

        <el-table-column prop="clickRate" :sortable="true" width="108" label="点击率">
          <template #default="scope">
            <div>{{ scope.row.clickRate }}%</div>

          </template>

        </el-table-column>

        <el-table-column prop="saveNumber" :sortable="true" width="108" label="收藏量"></el-table-column>

        <el-table-column prop="evaluationsNumber" :sortable="true" width="108" label="评论量"></el-table-column>

        <el-table-column label="操作" width="108" align="center">
          <template #default="scope">
            <div @click="handleDetail(scope.row)" style="color: rgb(17, 139, 221);cursor: pointer;">查看详情</div>

          </template>

        </el-table-column>

      </el-table>

      <!-- 分页组件区域 -->
      <div class="pager">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="houseQueryDto.current" :page-sizes="[10, 20]" :page-size="houseQueryDto.size"
                       layout="total, sizes, prev, pager, next, jumper" :total="apiResult.total"></el-pagination>

      </div>

    </div>

    <!-- 房源流量申请信息抽屉 -->
    <el-drawer title="房源流量申请信息" :modal="false" :wrapperClosable="false" :visible.sync="drawer"
               :direction="direction" size="70%" :before-close="handleClose">
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

    </el-drawer>

  </div>

</template>

<script>
// B站 「程序辰星」原创出品
import AutoInput from "@/components/AutoInput.vue"; // 自己封装好的输入框组件
export default {
  components: { AutoInput }, // 注册组件
  data() {
    return {
      drawer: false,
      direction: 'ttb',
      apiResult: { // 后端返回的查询数据的响应数据
        data: [], // 数据项
        total: 0, // 符合条件的数据总想 - 初始赋值为0
      },
      landlord: {}, // 房源流量信息
      houseQueryDto: { // 搜索条件
        current: 1, // 当前页 - 初始是第一页
        size: 10, // 页面显示大小 - 初始是10条
      },

    };
  },
  created() {
    this.fetchFreshData();
  },
  methods: {
    handleClose() {
      this.drawer = false;
    },
    handleDetail(data) {
      this.drawer = true;
      this.landlord = data;
    },
    // 输入框组件输入回传
    listener(text) {
      this.houseQueryDto.name = text; // 赋值查询条件的内容
      this.fetchFreshData(); // 重新加载数据
    },
    // 查询房源流量信息数据
    async fetchFreshData() {
      try {
        const { data, total } = await this.$axios.post('/house/listFlowIndex', this.houseQueryDto);
        this.apiResult.data = data;
        this.apiResult.total = total;
      } catch (error) {
        console.error('查询房源流量信息信息异常:', error);
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
  },
};
</script>

<style scoped lang="scss">
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
  padding-block: 10px;
  background-color: rgb(255, 255, 255);
}

.top-header {
  padding-inline: 10px;
  border-radius: 5px;
  padding-bottom: 20px;
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
