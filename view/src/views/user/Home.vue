<template>
  <div class="container-home">
    <!-- 房东认证 -->
    <div class="reply" v-if="landlord === null">
      <div class="title">
        空置房别闲置，你的房源值得更多租客看见
      </div>

      <div class="content">
        告别传统贴条招租，平台千万级流量曝光，精准匹配优质租客，让空房快速 “活” 起来，租金收益不空置。
        <span @click="reply" style="text-decoration: underline;cursor: pointer;">申请成为房东</span>

      </div>

    </div>

    <div class="content">
      <!-- 房屋列表信息 -->
      <div class="house-container">

        <!-- 房屋显示区域 -->
        <div class="house-list">
          <div @click="houseItemClick(item.id)" class="house-item" v-for="item in houseList" :key="item.id">
            <img :src="item.cover" alt="">
            <div>
              <div class="name">{{ item.name }}</div>

              <div class="point">
                <div>
                  <i class="el-icon-location"></i>

                  {{ item.cityAreaName }}&nbsp;·&nbsp;{{ item.communityName }}
                </div>

                <div>
                  {{ item.depositMethodName }}
                </div>

                <div>
                  {{ item.sizeNumber }}m²
                </div>

                <div>
                  {{ item.directionName }}
                </div>

                <div>
                  {{ item.fitmentStatusName }}
                </div>

              </div>

              <div class="rent">
                ¥{{ item.rent }}
              </div>

            </div>

          </div>

        </div>

        <!-- 分页区域 -->
        <div>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                         :current-page="houseQueryDto.current" :page-sizes="[10, 20]" :page-size="houseQueryDto.size"
                         layout="total, sizes, prev, pager, next, jumper" :total="total"></el-pagination>

        </div>

      </div>

      <!-- 额外拓展信息 -->
      <div class="right">
        测试数据
      </div>

    </div>

  </div>

</template>

<script>
export default {
  data() {
    return {
      landlord: {}, // 房东认证信息
      houseQueryDto: {},// 房屋查询条件
      houseList: [], // 房屋信息
      total: null, // 总页数
    }
  },
  created() {
    this.fetchLandlordData();
    this.fetchHouseData();
  },
  methods: {
    // 跳转至房屋详情页
    houseItemClick(id) {
      window.open(`/house-detail?id=${id}`, '_blank');
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
    reply() {
      window.open('/reply-landlord', '_blank');
    },
    async fetchHouseData() {
      try {
        const { data, total } = await this.$axios.post('/house/listUser', this.houseQueryDto);
        this.houseList = data;
        this.total = total;
      } catch (error) {
        console.log("查询房屋信息异常：", error);
      }
    },
    async fetchLandlordData() {
      try {
        const { data } = await this.$axios.post('/landlord/listUser', {});
        this.landlord = data;
        console.log(this.landlord);

      } catch (error) {
        console.log("查询房东申请信息异常：", error);
      }
    },
  }
}
</script>

<style scoped lang="scss">
.house-list {
  display: flex;
  flex-wrap: wrap;
  margin-block: 30px;

  .house-item {
    flex: 1 1 400px;
    display: flex;
    gap: 10px;
    padding: 10px;
    box-sizing: border-box;
    cursor: pointer;

    .name {
      font-size: 18px;
    }

    img {
      width: 120px;
      height: 80px;
      border-radius: 5px;
    }

    .point {
      font-size: 12px;
      margin-block: 10px;
      display: flex;
      justify-content: left;
      align-items: center;
      gap: 10px;
      box-sizing: border-box;

      div {
        background-color: rgb(245, 246, 247);
        padding: 2px 4px;
      }
    }

    .rent {
      font-size: 22px;
      font-weight: 800;
      color: rgb(222, 88, 78);
    }
  }
}

.content {
  display: flex;
  justify-content: space-between;
  gap: 1px;

  .house-container {
    width: 80%;
    background-color: rgb(255, 255, 255);
    padding: 20px 40px;
    box-sizing: border-box;
  }

  .right {
    width: 300px;
    background-color: rgb(255, 255, 255);
  }
}

.container-home {
  height: 2000px;
}

.reply {
  background-color: rgb(250, 250, 250);
  padding: 10px 20px;
  box-sizing: border-box;

  .title {
    font-size: 24px;
    font-weight: 800;
    margin-bottom: 10px;
    color: rgb(90, 89, 89);
  }

  .content {
    font-size: 14px;
  }
}
</style>
