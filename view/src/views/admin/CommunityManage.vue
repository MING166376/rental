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

        <div class="primary-bt" @click="drawer = true">
          <i class="el-icon-plus"></i>

          新增小区
        </div>

      </div>

    </div>

    <!-- 表格及分页信息 -->
    <div>
      <el-table :data="apiResult.data">
        <el-table-column width="200" prop="username" label="申请人">
          <template #default="scope">
            <div class="over-text">
              {{ scope.row.username }}
            </div>

          </template>

        </el-table-column>

        <el-table-column width="300" prop="content" label="身份证号">
          <template #default="scope">
            <div class="over-text">
              {{ scope.row.idcard }}
            </div>

          </template>

        </el-table-column>

        <el-table-column prop="parentId" :sortable="true" width="108" label="审核状态">
          <template #default="scope">
            <el-tag :type="scope.row.isAudit ? 'success' : 'danger'" size="mini">{{ scope.row.isAudit ? '已审核' : '未审核'
              }}</el-tag>

          </template>

        </el-table-column>

        <el-table-column prop="createTime" :sortable="true" width="168" label="申请时间"></el-table-column>

        <el-table-column label="" align="center">
          <template #default="scope">
            <div class="operate-buttons">
              <el-dropdown trigger="click" placement="bottom-end">
                <span class="el-dropdown-link">
                  <i class="el-icon-more"></i>

                </span>

                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="handleDetail(scope.row)"
                                    icon="el-icon-finished">详情</el-dropdown-item>

                  <el-dropdown-item @click.native="handleDelete(scope.row)" icon="el-icon-delete">删除</el-dropdown-item>

                </el-dropdown-menu>

              </el-dropdown>

            </div>

          </template>

        </el-table-column>

      </el-table>

      <!-- 分页组件区域 -->
      <div class="pager">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="communityQueryDto.current" :page-sizes="[10, 20]" :page-size="communityQueryDto.size"
                       layout="total, sizes, prev, pager, next, jumper" :total="apiResult.total"></el-pagination>

      </div>

    </div>

    <!-- 删除确认弹窗 -->
    <el-dialog title="删除小区信息" :show-close="false" :visible.sync="dialogDeletedVisible" width="20%">
      <span>确定删除小区信息数据？</span>

      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="dialogDeletedVisible = false">取消</el-button>

        <el-button size="mini" type="primary" @click="confirmDeleted">确定</el-button>

      </span>

    </el-dialog>

    <!-- 小区信息抽屉 -->
    <el-drawer title="小区信息操作" :modal="false" :wrapperClosable="false" :visible.sync="drawer" :direction="direction"
               size="40%" :before-close="handleClose">
      <div style="margin-inline: 20px;">
        <div>
          <el-steps :active="active" finish-status="success">
            <el-step title="补充地区信息"></el-step>

            <el-step title="补充小区基本信息"></el-step>

            <el-step title="补充小区介绍"></el-step>

          </el-steps>

        </div>

        <div>
          <!-- 小区地区信息 -->
          <div v-if="active === 0">
            <div>
              <p>*所属省份</p>

              <el-select @change="handleAreaChange" style="width: 100%;" v-model="topAreaId" placeholder="请选择">
                <el-option v-for="item in topArea" :key="item.id" :label="item.name" :value="item.id">
                </el-option>

              </el-select>

              <p>*所属市区</p>

              <el-select style="width: 100%;" v-model="cityAreaId" placeholder="请选择">
                <el-option v-for="item in cityArea" :key="item.id" :label="item.name" :value="item.id">
                </el-option>

              </el-select>

            </div>

          </div>

          <!-- 小区基本信息 -->
          <div v-if="active === 1">
            <div>
              <p>*小区标题</p>

              <el-input v-model="community.name" placeholder="请输入内容"></el-input>

            </div>

            <div>
              <p>*小区封面</p>

              <div class="user-avatar">
                <p>点击📷处即可上传小区封面</p>

                <img v-if="cover" style="width: 200px;height: 140px;border-radius: 5px;" :src="cover || ''" alt="">
                <el-upload class="avatar-uploader" action="api/v1.0/house-rental-api/file/upload"
                           :show-file-list="false" :on-success="handleImageSuccess">
                  <i class="el-icon-camera-solid"></i>

                </el-upload>

              </div>

            </div>

          </div>

          <!-- 小区介绍 -->
          <div v-if="active === 2">
            <div>
              <p>*小区实况图</p>

              <el-upload :on-success="handleCovers" action="api/v1.0/house-rental-api/file/upload"
                         list-type="picture-card" :on-preview="handlePictureCardPreview" :on-remove="handleRemove">
                <i class="el-icon-plus"></i>

              </el-upload>

              <el-dialog :modal="false" :visible.sync="dialogVisible">
                <img style="z-index: 1000;" width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>

            </div>

            <div>
              <p>*补充小区介绍</p>

              <Editor :receiveContent="content" height="300px" api="api/v1.0/house-rental-api/file/upload"
                      @on-listener="onListener" />
            </div>

          </div>

        </div>

        <div style="display: flex;margin-block: 20px;">
          <div v-if="active !== 0" class="primary-bt" @click="last" style="text-align: center;">
            <i class="el-icon-caret-left"></i>

            上一步
          </div>

          <div v-if="active !== 2" class="primary-bt" @click="next" style="text-align: center;">
            <i class="el-icon-caret-right"></i>

            下一步
          </div>

          <div v-if="active === 2" class="info-bt" @click="saveCommunity" style="text-align: center;">
            新增小区信息
          </div>

        </div>

      </div>

    </el-drawer>

  </div>

</template>

<script>
// B站 「程序辰星」原创出品
import AutoInput from "@/components/AutoInput.vue"; // 自己封装好的输入框组件
import Tab from "@/components/Tab.vue";
import Editor from "@/components/Editor.vue";
export default {
  components: { AutoInput, Tab, Editor }, // 注册组件
  data() {
    return {
      dialogImageUrl: '',
      content: '',
      dialogVisible: false,
      topAreaId: null,
      cityAreaId: null,
      topArea: [],
      cityArea: [],
      cover: '', // 小区封面
      active: 0,
      drawer: false,
      direction: 'rtl',
      id: null, // 页面即将删除的数据ID
      apiResult: { // 后端返回的查询数据的响应数据
        data: [], // 数据项
        total: 0, // 符合条件的数据总想 - 初始赋值为0
      },
      community: {}, // 小区信息
      communityQueryDto: { // 搜索条件
        current: 1, // 当前页 - 初始是第一页
        size: 10, // 页面显示大小 - 初始是10条
      },
      coverList: [], // 小区实况图图片链接数组
      dialogDeletedVisible: false, // 删除弹窗控制开关变量 - 初始是关（false）
    };
  },
  created() {
    this.fetchFreshData();
    this.fetchTopArea();
  },
  methods: {
    onListener(text) {
      this.content = text;
    },
    handleCovers(response, file, fileList) {
      this.coverList.push(response.data);
      console.log("上传，此时的图片数组：", this.coverList);
    },
    handleRemove(file, fileList) {
      if (fileList.length === 0) return;
      this.coverList = this.coverList.filter(entity => entity !== fileList[0].response.data);
      console.log("移除，此时的图片数组：", this.coverList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 新增小区信息
    async saveCommunity() {
      try {
        this.community.cover = this.cover;
        this.community.detail = this.content;
        this.community.areaId = this.cityAreaId;
        this.community.covers = this.coverList.length === 0 ? null : this.coverList.join(',');
        await this.$axios.post('/community/save', this.community);
        this.handleClose();
      } catch (error) {
        console.log("新增小区信息异常：", error);
        this.$notify({
          title: '小区新增',
          type: 'info',
          message: error.message,
          position: 'buttom-right',
          suration: 1000,
        })
      }
    },
    // 头像上传响应
    handleImageSuccess(res, file) {
      // 通知提示
      this.$notify({
        title: '头像上传',
        type: res.code === 200 ? 'success' : 'error',
        message: res.code === 200 ? '上传成功' : res.data,
        position: 'buttom-right',
        suration: 1000,
      })
      if (res.code === 200) {
        this.cover = res.data; // 响应里面的data，即后端返回的上传后的图片链接
      }
    },
    async fetchTopArea() {
      try {
        const areaQueryDto = { parentId: 0 }
        const { data } = await this.$axios.post('/area/list', areaQueryDto);
        this.topArea = data;
      } catch (error) {
        console.log("查询省份信息异常：", error);
      }
    },
    async handleAreaChange() {
      this.cityAreaId = null;
      try {
        const areaQueryDto = { parentId: this.topAreaId }
        const { data } = await this.$axios.post('/area/list', areaQueryDto);
        this.cityArea = data;
      } catch (error) {
        console.log("查询省份下的市区信息异常：", error);
      }
    },
    last() {
      if (this.active-- <= 0) this.active = 2;
    },
    next() {
      if (this.active++ > 2) this.active = 0;
    },
    handleChange(val) {
      this.communityQueryDto.isAudit = Number(val.value);
      this.fetchFreshData();
    },
    handleClose() {
      this.drawer = false;
      this.cover = '';
      this.covers = [];
      this.content = '';
      this.community = {};
    },
    handleDetail(data) {
      this.drawer = true;
      this.community = data;
    },
    // 输入框组件输入回传
    listener(text) {
      this.communityQueryDto.idcard = text; // 赋值查询条件的内容
      this.fetchFreshData(); // 重新加载数据
    },
    async auditcommunity() {
      try {
        const community = {
          id: this.community.id,
          isAudit: true,
        }
        await this.$axios.put('/community/update', community);
        this.$message.success('审核成功');
        this.drawer = false; // 关闭详情抽屉
        this.fetchFreshData(); // 重新加载小区数据
      } catch (error) {
        this.$message.info(error.message);
        console.error('审核小区信息信息异常:', error);
      }
    },
    // 查询小区信息数据
    async fetchFreshData() {
      try {
        const { data, total } = await this.$axios.post('/community/list', this.communityQueryDto);
        this.apiResult.data = data;
        this.apiResult.total = total;
      } catch (error) {
        console.error('查询小区信息信息异常:', error);
      }
    },
    // 分页 - 处理页面页数切换
    handleSizeChange(size) {
      this.communityQueryDto.size = size; // 当前页面大小重置
      this.communityQueryDto.currrent = 1; // 当前页设置为第一页
      this.fetchFreshData(); // 重新加载页面数据
    },
    // 分页 - 处理页面当前页切换
    handleCurrentChange(current) {
      this.communityQueryDto.current = current; // 当前页选中
      this.fetchFreshData(); // 重新加载页面数据
    },
    // 表格点击删除小区信息
    handleDelete(row) {
      this.dialogDeletedVisible = true; // 开启删除弹窗确认
      this.id = row.id;
    },
    // 小区信息删除
    async confirmDeleted() {
      try {
        const { code } = await this.$axios.delete(`/community/${this.id}`);
        if (code === 200) {
          this.$notify.success({
            title: '小区信息删除',
            message: '删除成功',
            position: 'buttom-right',
            suration: 1000,
          });
          this.dialogDeletedVisible = false; // 关闭删除确认弹窗
          this.id = null; // 将标识ID置位
          this.fetchFreshData(); // 删除小区信息数据之后，重新加载小区信息数据
        }
      } catch (error) {
        console.log("删除小区信息数据异常：", error);
      }
    }
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
  margin: 10px 20px;
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
