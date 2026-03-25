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

        <!-- 额外查询条件区域 -->
        <div>
          <!-- 搜索小区 -->
          <div class="name">
            <el-input clearable style="width: 300px;" placeholder="请输入内容" v-model="houseQueryDto.name">
              <template slot="append">
                <div @click="fetchHouseData">
                  搜索
                </div>

              </template>

            </el-input>

          </div>

          <!-- 常居住地信息 -->
          <div class="area">
            {{ userArea.topAreaName }} > {{ userArea.cityAreaName }}
          </div>

          <!-- 房屋属性查询条件 -->
          <div class="condition-container">
            <!-- 房屋面积-->
            <div class="condition">
              <div class="point-text">
                面积
              </div>

              <div class="item-condition">
                <div @click="houseSizeNumberClick(item)" v-for="(item, index) in houseSizeNumberList"
                     :key="item.value">
                  {{ item.label }}
                  <span v-if="index !== 0">m²</span>

                </div>

              </div>

            </div>

            <!-- 租金-->
            <div class="condition">
              <div class="point-text">
                租金
              </div>

              <div class="item-condition">
                <div @click="houseRentClick(item)" v-for="(item, index) in houseRentRangeList"
                     :key="item.value">
                  {{ item.label }}
                  <span v-if="index !== 0">元</span>

                </div>

              </div>

            </div>

            <!-- 房屋类型 -->
            <div class="condition">
              <div class="point-text">
                类型
              </div>

              <div class="item-condition">
                <div @click="houseTypeClick(item)" v-for="item in houseTypeList" :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

            <!-- 房屋朝向 -->
            <div class="condition">
              <div class="point-text">
                朝向
              </div>

              <div class="item-condition">
                <div @click="houseDirectionClick(item)" v-for="item in houseDirectionList"
                     :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

            <!-- 房屋户型 -->
            <div class="condition">
              <div class="point-text">
                户型
              </div>

              <div class="item-condition">
                <div @click="houseSizedClick(item)" v-for="item in houseSizedList" :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

            <!-- 房屋租金方式 -->
            <div class="condition">
              <div class="point-text">
                租金方式
              </div>

              <div class="item-condition">
                <div @click="houseDepositMethodClick(item)" v-for="item in houseDepositMethodList"
                     :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

            <!-- 房屋是否临近地铁 -->
            <div class="condition">
              <div class="point-text">
                临近地铁
              </div>

              <div class="item-condition">
                <div @click="houseSubwayClick(item)" v-for="item in houseSubwayList" :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

            <!-- 房屋装修状态-->
            <div class="condition">
              <div class="point-text">
                装修状态
              </div>

              <div class="item-condition">
                <div @click="houseFitmentStatusClick(item)" v-for="item in houseFitmentStatusList"
                     :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

            <!-- 房屋租赁方式-->
            <div class="condition">
              <div class="point-text">
                租赁方式
              </div>

              <div class="item-condition">
                <div @click="houseRentalTypeClick(item)" v-for="item in houseRentalTypeList"
                     :key="item.value">
                  {{ item.label }}
                </div>

              </div>

            </div>

          </div>

        </div>

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
      userArea: {}, //用户常居住地信息
      landlord: {}, // 房东认证信息
      houseQueryDto: {},// 房屋查询条件
      houseList: [], // 房屋信息
      total: null, // 总页数
      houseTypeList: [], // 房屋类型查询条件数组
      houseDirectionList: [],// 房屋朝向查询条件数组
      houseSizedList: [],// 房屋户型查询条件数组
      houseDepositMethodList: [],// 房屋租金方式查询条件数组
      houseSubwayList: [], // 房屋是否临近地铁查询条件数组
      houseFitmentStatusList: [], // 房屋装修状态查询条件数组
      houseRentalTypeList: [],// 房屋租赁方式查询条件数组
      houseSizeNumberList: [], // 房屋面积查询条件数组
      houseRentRangeList: [], // 房屋租金查询条件数组
    }
  },
  created() {
    this.fetchLandlordData();
    this.fetchHouseData();
    this.fetchUserAreaData();
    this.fetchHouseType();
    this.fetchHouseDirection();
    this.fetchHouseSized();
    this.fetchHouseDepositMethod();
    this.fetchHouseSubway();
    this.fetchHouseFitmentStatus();
    this.fetchHouseRentalType();
    this.fetchHouseSizeNumber();
    this.fetchHouseRent();
  },
  methods: {
    houseTypeClick(item) {
      this.houseQueryDto.typeId = item.value;
      this.fetchHouseData();
    },
    houseDirectionClick(item) {
      this.houseQueryDto.directionId = item.value;
      this.fetchHouseData();
    },
    houseSizedClick(item) {
      this.houseQueryDto.sizedId = item.value;
      this.fetchHouseData();
    },
    houseDepositMethodClick(item) {
      this.houseQueryDto.depositMethodId = item.value;
      this.fetchHouseData();
    },
    houseSubwayClick(item) {
      this.houseQueryDto.isSubway = item.value === 1;
      this.fetchHouseData();
    },
    houseFitmentStatusClick(item) {
      this.houseQueryDto.fitmentStatusId = item.value;
      this.fetchHouseData();
    },
    houseRentalTypeClick(item) {
      this.houseQueryDto.rentalType = item.value;
      this.fetchHouseData();
    },
    houseSizeNumberClick(item) {
      // 如果说不限制
      if (item.value === null) {
        this.houseQueryDto.minSizeNumber = null;
        this.houseQueryDto.maxSizeNumber = null;
      } else {
        const areaRange = item.label.split('-');
        this.houseQueryDto.minSizeNumber = areaRange[0];
        this.houseQueryDto.maxSizeNumber = areaRange[1];
      }
      this.fetchHouseData();
    },
    houseRentClick(item) {
      // 如果说不限制
      if (item.value === null) {
        this.houseQueryDto.minRent = null;
        this.houseQueryDto.maxRent = null;
      } else {
        const rentRange = item.label.split('-');
        this.houseQueryDto.minRent = rentRange[0];
        this.houseQueryDto.maxRent = rentRange[1];
      }
      this.fetchHouseData();
    },
    // 查询房屋类型
    async fetchHouseType() {
      try {
        const { data } = await this.$axios.get('/house/houseTypeList');
        this.houseTypeList = data;
        this.houseTypeList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋类型异常：", error);
      }
    },
    // 查询房屋朝向
    async fetchHouseDirection() {
      try {
        const { data } = await this.$axios.get('/house/houseDirectionList');
        this.houseDirectionList = data;
        this.houseDirectionList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋朝向异常：", error);
      }
    },
    // 查询房屋户型
    async fetchHouseSized() {
      try {
        const { data } = await this.$axios.get('/house/houseSizedList');
        this.houseSizedList = data;
        this.houseSizedList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋户型异常：", error);
      }
    },
    // 查询房屋押金方式数组
    async fetchHouseDepositMethod() {
      try {
        const { data } = await this.$axios.get('/house/houseDepositMethodList');
        this.houseDepositMethodList = data;
        this.houseDepositMethodList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋户型异常：", error);
      }
    },
    // 查询房屋临近地铁数组
    async fetchHouseSubway() {
      try {
        const { data } = await this.$axios.get('/house/houseSubwayList');
        this.houseSubwayList = data;
        this.houseSubwayList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋是否临近地铁异常：", error);
      }
    },
    // 查询房屋装修状态数组
    async fetchHouseFitmentStatus() {
      try {
        const { data } = await this.$axios.get('/house/houseFitmentStatusList');
        this.houseFitmentStatusList = data;
        this.houseFitmentStatusList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋装修状态异常：", error);
      }
    },
    // 查询房屋租赁方式数组
    async fetchHouseRentalType() {
      try {
        const { data } = await this.$axios.get('/house/houseRentalTypeList');
        this.houseRentalTypeList = data;
        this.houseRentalTypeList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋租赁方式异常：", error);
      }
    },
    // 查询房屋面积查询条件数组
    async fetchHouseSizeNumber() {
      try {
        const { data } = await this.$axios.get('/house/houseSizeNumber');
        this.houseSizeNumberList = data;
        this.houseSizeNumberList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋面积查询条件异常：", error);
      }
    },
    // 查询房屋租金查询条件数组
    async fetchHouseRent() {
      try {
        const { data } = await this.$axios.get('/house/houseRentRange');
        this.houseRentRangeList = data;
        this.houseRentRangeList.unshift({ value: null, label: '不限' });
      } catch (error) {
        console.log("查询房屋租金查询条件异常：", error);
      }
    },
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
    async fetchUserAreaData() {
      try {
        const { data } = await this.$axios.post('/user-area/listUser', {});
        this.userArea = data[0];

      } catch (error) {
        console.log("查询用户常居住地信息异常：", error);
      }
    },
  }
}
</script>

<style scoped lang="scss">
.condition-container {
  border: 1px solid rgb(240, 240, 240);
  padding: 10px 20px;
  margin-block: 10px;
}

.condition {
  display: flex;
  justify-content: left;
  align-items: center;
  margin-block: 10px;
  border-bottom: 1px solid rgb(240, 240, 240);
  padding-block: 8px;

  .point-text {
    min-width: 60px;
    font-size: 12px;
    color: rgb(156, 158, 160);
  }

  .item-condition {
    display: flex;
    font-size: 12px;

    div {
      margin-right: 20px;

      &:hover {
        cursor: pointer;
        color: rgb(31, 176, 129);
      }
    }
  }
}

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
