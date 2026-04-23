<template>
  <div class="container">
    <div class="house-news-container">
      <!-- 左边列表 -->
      <div class="left">
        <div class="top-header">
          <div class="nav-left">
            <h2>房屋资讯</h2>
          </div>
          <div class="nav-right">
            <AutoInput placeholder="资讯标题" @listener="listener" />
          </div>
        </div>

        <div @click="houseNewsClick(item)" class="house-item" v-for="item in apiResult.data" :key="item.id">
          <img :src="item.cover" alt="封面图片" />
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
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="houseNewsQueryDto.current"
              :page-sizes="[10, 20]"
              :page-size="houseNewsQueryDto.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="apiResult.total"
          >
          </el-pagination>
        </div>
      </div>

      <!-- 右侧推荐区域（缩小并固定） -->
      <div class="right">
        <h2 style="margin-top: 0">资讯推荐</h2>
        <div @click="houseNewsClick(news)" class="item-news" v-for="(news, index) in houseRecommedNewsList" :key="index">
          <img :src="news.cover" alt="" />
          <div class="text-item">
            {{ news.title }}
          </div>
        </div>
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
      houseRecommedNewsList: [],
      apiResult: {
        data: [],
        total: 0,
      },
      houseNewsQueryDto: {
        current: 1,
        size: 10,
      },
      recommendCount: 3,
    };
  },
  created() {
    this.fetchFreshData();
    this.fetchRecommendHouseNews(this.recommendCount);
  },
  methods: {
    async fetchRecommendHouseNews(count) {
      try {
        const { data } = await this.$axios.get(`/house-news/recommend/${count}`);
        this.houseRecommedNewsList = data;
      } catch (error) {
        this.$message.error("获取推荐的房屋资讯信息失败");
        console.error(error);
      }
    },
    houseNewsClick(item) {
      window.open(`/house-news-detail?id=${item.id}`, "_blank");
    },
    listener(text) {
      this.houseNewsQueryDto.title = text;
      this.fetchFreshData();
    },
    async fetchFreshData() {
      try {
        const { data, total } = await this.$axios.post("/house-news/list", this.houseNewsQueryDto);
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
    },
  },
};
</script>

<style scoped>
/* 只调整尺寸和布局，保留原有颜色 */
.container {
  padding: 20px;
  background-color: #fff;
}

.house-news-container {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.left {
  flex: 1;
  min-width: 0;
}

/* 右侧区域：固定宽度且缩小 */
.right {
  width: 240px; /* 原400px缩小 */
  flex-shrink: 0;
  margin-top: 52px; /* 与左侧标题对齐 */
}

.right h2 {
  font-size: 1.2rem; /* 标题稍小 */
  margin-bottom: 16px;
}

.item-news {
  position: relative;
  margin-bottom: 12px;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
}

.item-news img {
  width: 100%;
  height: 120px; /* 固定高度，更紧凑 */
  object-fit: cover;
  display: block;
  border-radius: 5px;
}

.text-item {
  position: absolute;
  bottom: 5px;
  font-weight: 900;
  width: 100%;
  padding: 8px; /* 稍减小内边距 */
  font-size: 12px; /* 字体缩小 */
  box-sizing: border-box;
  color: rgb(255, 255, 255);
  background-color: rgba(0, 0, 0, 0.3);
}

.top-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 10px;
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

.pager {
  display: flex;
  justify-content: right;
  margin-top: 20px;
}

/* 响应式：小屏时右侧换行 */
@media (max-width: 900px) {
  .house-news-container {
    flex-wrap: wrap;
  }
  .right {
    width: 100%;
    margin-top: 20px;
  }
}

/* 隐藏未使用的样式（保留但不影响布局） */
.actions,
.drawer-content,
.cover-upload,
.uploader,
.upload-tip {
  display: none;
}
</style>