<template>
  <div class="feature__image">
    <div class="broadcast__animation"></div>
    <div class="broadcast__animation second"></div>
    <div class="broadcast__animation third"></div>
    <img src="../../assets/svg/pepole.svg" alt="" />
  </div>
</template>

<script>
export default {
  name: "broadcast"
};
</script>

<style scoped>
img {
  max-width: 90%;
  min-width: 50%;
}
.feature__image {
  max-width: 80%;
  min-width: 75%;
  overflow: hidden;
  position: relative;
  text-align: center;
}
.broadcast__animation {
  border-color: #eec300 !important;
}
.broadcast__animation {
  position: absolute;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  border: 5px solid #00000000;
  animation: broadcast-ani--1 2s infinite;
}
.second {
  border-color: #7a44be !important;
}
.second {
  position: absolute;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  border: 5px solid #00000000;
  animation: broadcast-ani--1 2.5s infinite;
}
.third {
  border-color: #f42a00 !important;
}
.third {
  position: absolute;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  border: 5px solid #00000000;
  animation: broadcast-ani--1 2.8s infinite;
}
@keyframes broadcast-ani--1 {
  0% {
    opacity: 1;
    width: 80px;
    height: 80px;
  }
  50% {
    opacity: 0;
    width: 350px;
    height: 350px;
  }
  100% {
    opacity: 0;
  }
}
</style>
