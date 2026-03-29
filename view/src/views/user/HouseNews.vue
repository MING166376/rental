<template>
  <div class="container">

    <div class="house-news-container">
      <!-- 左边列表 -->
      <div class="left">
        <div class="top-header">
          <div class="nav-right">
            <AutoInput placeholder="资讯标题" @listener="listener" />
          </div>

        </div>

        <div @click="houseNewsClick(item)" class="house-item" v-for="item in apiResult.data" :key="item.id">
          <img :src="item.cover" alt="封面图片">
          <div class="house-content">
            <div class="name">{{ item.title }}</div>

            <div class="summary">{{ item.summary }}</div>

            <div class="meta">
              <div class="stats">
                <span><i class="el-icon-star-on"></i> {{ item.viewNumber }}</span>

                <span><i class="el-icon-view"></i> {{ item.saveNumber }}</span>

                <span>发布于{{ item.createTime }}</span>

              </div>

            </div>

          </div>

        </div>

        <div class="pager">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                         :current-page="houseNewsQueryDto.current" :page-sizes="[10, 20]"
                         :page-size="houseNewsQueryDto.size" layout="total, sizes, prev, pager, next, jumper"
                         :total="apiResult.total">
          </el-pagination>

        </div>

      </div>

      <!-- 推荐区域 -->
      <div class="right">
        推荐
      </div>

    </div>

  </div>

</template>

<script>
import AutoInput from "@/components/AutoInput.vue";

export default {
  components: { AutoInput },
  data() {
    return {
      apiResult: {
        data: [],
        total: 0,
      },
      houseNewsQueryDto: {
        current: 1,
        size: 10,
      },
    };
  },
  created() {
    this.fetchFreshData();
  },
  methods: {
    // 房源资讯点击之后,跳转至详情页
    houseNewsClick(item) {
      window.open(`/house-news-detail?id=${item.id}`, '_blank');
    },
    async houseNewsUpdate(item) {
      try {
        const { data } = await this.$axios.get(`/house-news/getById/${item.id}`);
        this.houseNews = { ...data };
        this.content = data.content;
        this.cover = data.cover;
      } catch (error) {
        this.$notify.error({
          title: '错误',
          message: error.message,
          duration: 2000,
        });
      }
    },
    listener(text) {
      this.houseNewsQueryDto.title = text;
      this.fetchFreshData();
    },
    async fetchFreshData() {
      try {
        const { data, total } = await this.$axios.post('/house-news/list', this.houseNewsQueryDto);
        this.apiResult.data = data;
        this.apiResult.total = total;
      } catch (error) {
        this.$message.error(error.message);
      }
    },
    handleSizeChange(size) {
      this.houseNewsQueryDto.size = size;
      this.houseNewsQueryDto.current = 1;
      this.fetchFreshData();
    },
    handleCurrentChange(current) {
      this.houseNewsQueryDto.current = current;
      this.fetchFreshData();
    }
  },
};
</script>

<style scoped>
.actions {
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

.container {
  padding: 20px;
  background-color: #fff;
}

.top-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.house-news-container {
  margin-bottom: 20px;
  display: flex;
  gap: 20px;

  .right {
    width: 400px;
  }
}

.house-item {
  display: flex;
  gap: 20px;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.house-item img {
  width: 160px;
  height: 120px;
  border-radius: 4px;
  object-fit: cover;
}

.house-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.name {
  font-size: 22px;
  font-weight: 800;
  cursor: pointer;
  color: #333;
}

.summary {
  font-size: 12px;
  color: #666;
  line-height: 1.5;
  background-color: #fafafa;
  padding: 8px;
  border-radius: 4px;
}

.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.stats {
  display: flex;
  justify-content: left;
  align-items: center;
  gap: 15px;
  font-size: 14px;
  color: #888;
}

.actions {
  display: flex;
  gap: 10px;
}

.drawer-content {
  padding: 20px;
}

.cover-upload {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cover-upload img {
  width: 200px;
  height: 140px;
  border-radius: 4px;
  object-fit: cover;
}

.uploader {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  cursor: pointer;
  color: #999;
}

.uploader:hover {
  border-color: #409EFF;
}

.upload-tip {
  margin-top: 8px;
  font-size: 12px;
}

.pager {
  display: flex;
  justify-content: left;
  margin-top: 20px;
}
</style>
