<template>
  <div class="container-center">
    <div class="top">
      <div class="logo">
        <div>
          <img src="/logo.png" alt="" srcset="">
        </div>

        <div class="text">
          房屋租赁系统·服务中心
        </div>

      </div>

      <div class="info">
        <div>
          <img :src="userInfo.avatar" alt="" srcset="">
        </div>

        <div class="text">
          {{ userInfo.username }}
        </div>

      </div>

    </div>

    <div class="service-cotainer">
      <div class="left">
        <el-menu style="border-right: 0;" default-active="2" class="el-menu-vertical-demo" @open="handleOpen"
                 @close="handleClose">
          <el-menu-item index="1">
            <i class="el-icon-document"></i>

            <span slot="title">房东信息</span>

          </el-menu-item>

          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-shop"></i>

              <span>房源管理</span>

            </template>

            <el-menu-item-group>
              <el-menu-item index="2-1">我发布的房源</el-menu-item>

              <el-menu-item index="2-2">流量统计</el-menu-item>

            </el-menu-item-group>

          </el-submenu>

          <el-menu-item index="3">
            <i class="el-icon-s-finance"></i>

            <span slot="title">预约看房</span>

          </el-menu-item>

        </el-menu>

      </div>

      <div class="right">

      </div>

    </div>

  </div>

</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
    }
  },
  created() {
    this.fetchUserInfo();
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    async fetchUserInfo() {
      try {
        const { data } = await this.$axios.get('/user/auth');
        this.userInfo = data;
      } catch (error) {
        this.$router.push('/user');
      }
    },
  }
}
</script>

<style scoped lang="scss">
.container-center {
  width: 100%;
  height: 100vh;
  background-color: rgb(246, 248, 250);

  .top {
    height: 60px;
    width: 100%;
    padding-inline: 20px;
    box-sizing: border-box;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 6px 8px rgb(240, 240, 240);
    display: flex;
    justify-content: space-between;
    align-items: center;

    .logo {
      display: flex;
      justify-content: left;
      align-items: center;
      gap: 10px;

      img {
        width: 30px;
        height: 30px;
      }

      .text {
        font-size: 20px;
      }

    }

    .info {
      display: flex;
      justify-content: left;
      align-items: center;
      gap: 6px;

      img {
        width: 25px;
        height: 25px;
        border-radius: 50%;
      }

      .text {
        font-size: 16px;
      }

    }


  }
}

.service-cotainer {
  display: flex;

  .left {
    width: 253px;
    height: calc(100vh - 60px);
    background-color: rgb(255, 255, 255);
  }
}
</style>
