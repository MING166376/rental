<template>
  <div class="main-container">
    <div class="left">
      <div class="static-value">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item, index) in staticValues" :key="index">
            <div>
              <el-statistic group-separator="," :precision="0" :value="item.count"
                            :title="item.name"></el-statistic>

            </div>

          </el-col>

        </el-row>

      </div>

      <div style="margin-top: 30px;">
        <LineChart :tooltipFormatter="customTooltipFormatter" height="450px" tag="房源存量趋势" :values="values"
                   :date="dateList" @on-selected="onSelected" />
      </div>

    </div>

    <div class="right">

    </div>

  </div>

</template>

<script>
import LineChart from "@/components/LineChart"
export default {
  components: { LineChart },
  data() {
    return {
      staticValues: [],
      values: [], // y轴
      dateList: [], // x轴
      defaultSelectDays: 365, //默认查一年
    }
  },
  created() {
    this.fetchStaticValues();
    this.fetchHouseLineChart(this.defaultSelectDays);
  },
  methods: {
    // 自定义提示框格式
    customTooltipFormatter(params) {
      // params是一个数组，包含当前点的所有信息
      const date = params[0].axisValue; // 获取日期
      const value = params[0].data;     // 获取数值
      return `${date}号，共上线${value}套房源`;
    },
    onSelected(day) {
      this.fetchHouseLineChart(day);
    },
    async fetchHouseLineChart(day) {
      try {
        const { data } = await this.$axios.get(`/dashboard/houseLineChart/${day}`);
        this.dateList = data.map(entity => entity.name); // 处理成x轴数据格式
        this.values = data.map(entity => entity.count); // 处理成y轴数据格式
      } catch (error) {
        console.error('房源存量趋势统计异常:', error);
      }
    },
    async fetchStaticValues() {
      try {
        const { data } = await this.$axios.get('/dashboard/mainStatic');
        this.staticValues = data;
      } catch (error) {
        console.error('静态数据统计异常:', error);
      }
    },
  }
};
</script>

<style scoped lang="scss">
.main-container {
  display: flex;
  width: 100%;

  .left {
    width: 65%;
    padding-inline: 30px;
    box-sizing: border-box;

    .static-value {
      background-color: rgb(248, 248, 248);
      padding: 20px;
      box-sizing: border-box;
      border-radius: 5px;
    }
  }

  .right {
    width: 35%;
  }
}
</style>
