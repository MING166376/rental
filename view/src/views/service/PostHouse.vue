<template>
  <div class="community-container">
    <div class="nav">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item style="cursor: pointer;" @click.native="toLastPage">房源管理</el-breadcrumb-item>

        <el-breadcrumb-item>房源新增</el-breadcrumb-item>

      </el-breadcrumb>

    </div>

    <!-- 小区信息区域 -->
    <div class="update-area">
      <!-- 小区名称 -->
      <div class="area">
        <span>小区名</span>

        <el-input style="width: 600px;" v-model="communityInfo.name" placeholder="小区名"></el-input>

      </div>

      <!-- 省份信息 -->
      <div class="area">
        <span>所属位置</span>

        <div style="display: flex;gap: 20px;">
          <el-select @change="handleAreaChange" style="width: 290px;" v-model="topAreaId" placeholder="请选择">
            <el-option v-for="item in topArea" :key="item.id" :label="item.name" :value="item.id">
            </el-option>

          </el-select>

          <el-select style="width: 290px;" v-model="cityAreaId" placeholder="请选择">
            <el-option v-for="item in cityArea" :key="item.id" :label="item.name" :value="item.id">
            </el-option>

          </el-select>

        </div>

      </div>

      <!-- 封面 -->
      <div class="area">
        <span>封面</span>

        <div class="user-avatar">
          <p style="font-size: 12px;color: rgb(0, 119, 184);">点击📷处即可上传小区封面</p>

          <img v-if="cover" style="width: 290px;height: 170px;border-radius: 5px;" :src="cover || ''" alt="">
          <el-upload class="avatar-uploader" action="api/v1.0/house-rental-api/file/upload"
                     :show-file-list="false" :on-success="handleImageSuccess">
            <i class="el-icon-camera-solid"></i>

          </el-upload>

        </div>

      </div>

      <!-- 实况图 -->
      <div class="area">
        <span>实况图</span>

        <div>
          <el-upload :file-list="coverList" :on-success="handleCovers"
                     action="api/v1.0/house-rental-api/file/upload" list-type="picture-card"
                     :on-preview="handlePictureCardPreview" :on-remove="handleRemove">
            <i class="el-icon-plus"></i>

          </el-upload>

          <el-dialog :modal="false" :visible.sync="dialogVisible">
            <img style="z-index: 1000;" width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>

        </div>

      </div>

      <!-- 小区介绍 -->
      <div class="area">
        <span>小区介绍</span>

        <div>
          <Editor style="width: 700px;" :receiveContent="content" height="300px"
                  api="api/v1.0/house-rental-api/file/upload" @on-listener="onListener" />
        </div>

      </div>

      <div class="area">
        <div class="info-bt" @click="updateCommunity"
             style="text-align: center;width: 200px;margin-left: 130px;margin-top: 30px;">
          修改小区信息
        </div>

      </div>

    </div>

  </div>

</template>

<script>
import Editor from "@/components/Editor.vue";
export default {
  components: { Editor },
  name: "CommunityUpdate",
  data() {
    return {
      communityInfo: {},
      name: null,
      cover: '',
      dialogImageUrl: '',
      dialogVisible: false,
      coverList: [],
      topArea: [], //省份信息
      cityArea: [], // 市区信息
      topAreaId: null, // 省份信息
      cityAreaId: null, // 城市ID
      content: '',
    }
  },
  created() {
    this.paramGet();
    this.fetchTopArea();
  },
  methods: {
    async paramGet() {
      const jsonCommunityInfo = localStorage.getItem('communityInfo');
      this.communityInfo = JSON.parse(jsonCommunityInfo);
      this.name = this.communityInfo.name;
      this.cover = this.communityInfo.cover;
      this.content = this.communityInfo.detail;
      this.coverList = this.communityInfo.length === 0 ? [] : this.communityInfo.covers.split(',').map(entity => {
        return {
          uid: Date.now() + Math.floor(Math.random() * 1000),
          url: entity
        }
      });
    },
    async updateCommunity() {
      try {
        this.communityInfo.cover = this.cover;
        this.communityInfo.detail = this.content;
        this.communityInfo.areaId = this.cityAreaId;
        this.communityInfo.covers = this.coverList.length === 0 ? null : this.coverList.map(entity => entity.url).join(',');
        await this.$axios.put('/community/update', this.communityInfo);
        this.$notify({
          title: '小区修改',
          type: 'success',
          message: '小区修改成功',
          position: 'buttom-right',
          suration: 1000,
        })
        this.toLastPage();
      } catch (error) {
        console.log("修改小区信息异常：", error);
        this.$notify({
          title: '小区修改',
          type: 'info',
          message: error.message,
          position: 'buttom-right',
          suration: 1000,
        })
      }
    },
    onListener(text) {
      this.content = text;
    },
    async fetchTopArea() {
      try {
        const areaQueryDto = { parentId: 0 }
        const { data } = await this.$axios.post('/area/list', areaQueryDto);
        this.topArea = data;
        this.topAreaId = this.communityInfo.topAreaId;
        this.handleAreaChange();
      } catch (error) {
        console.log("查询省份信息异常：", error);
      }
    },
    async handleAreaChange() {
      try {
        const areaQueryDto = { parentId: this.topAreaId }
        const { data } = await this.$axios.post('/area/list', areaQueryDto);
        this.cityArea = data;
        this.cityAreaId = this.communityInfo.areaId;
      } catch (error) {
        console.log("查询省份下的市区信息异常：", error);
      }
    },
    handleCovers(response, file, fileList) {
      this.coverList.push({
        uid: Date.now() + Math.floor(Math.random() * 1000),
        url: response.data
      });
      console.log("上传，此时的图片数组：", this.coverList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file, fileList) {
      console.log("file:", file);

      if (fileList.length === 0) return;
      this.coverList = this.coverList.filter(entity => entity.uid !== file.uid);
    },
    handleImageSuccess(res, file) {
      // 通知提示
      this.$notify({
        title: '封面上传',
        type: res.code === 200 ? 'success' : 'error',
        message: res.code === 200 ? '上传成功' : res.data,
        position: 'buttom-right',
        suration: 1000,
      })
      if (res.code === 200) {
        this.cover = res.data; // 响应里面的data，即后端返回的上传后的图片链接
      }
    },
    toLastPage() {
      this.$router.go(-1); // 返回上一页
    },
  }
};
</script>

<style scoped lang="scss">
.update-area {
  padding-block: 30px;
  background-color: rgb(255, 255, 255);
}

.area {
  margin-block: 10px;
  display: flex;
  justify-content: left;
  align-items: center;

  span {
    width: 120px;
    display: inline-block;
    text-align: right;
    margin-right: 10px;
    font-size: 12px;
    color: rgb(51, 51, 51);
  }
}

.community-container {
  padding: 10px 24px;
  box-sizing: border-box;

  .nav {
    background-color: rgb(246, 247, 248);
    padding: 12px 20px;
    border-bottom: 1px solid rgb(230, 230, 230);
  }
}
</style>
