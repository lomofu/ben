<template>
  <div class="box">
    <div class="stormy"></div>
    <div class="text2 d-flex justify-center ">403</div>
    <div class="text1 d-flex justify-center ">没有权限</div>
  </div>
</template>

<script>
export default {
  name: "403"
};
</script>

<style scoped>
.box {
  height: 100vh;
  position: relative;
  background: #575757;
}
.text1 {
  font-weight: bold;
  font-size: 30px;
  position: absolute;
  width: 100%;
  top: 55%;
  color: #aeaeae;
}
.text2 {
  font-weight: bold;
  font-size: 120px;
  position: absolute;
  width: 100%;
  top: 35%;
  text-shadow: 0 0 10px #00000029;
  color: white;
}
.stormy {
  -webkit-animation: stormy 5s ease-in-out infinite;
  animation: stormy 5s ease-in-out infinite;
  background: #222222;
  border-radius: 50%;
  box-shadow: #222222 65px -15px 0 -5px, #222222 25px -25px, #222222 30px 10px,
    #222222 60px 15px 0 -10px, #222222 85px 5px 0 -5px;
  height: 50px;
  width: 50px;
  margin-left: -60px;
  position: absolute;
  left: 50%;
  top: 30%;
}
.stormy:before {
  -webkit-animation: stormy_thunder 2s steps(1, end) infinite;
  animation: stormy_thunder 2s steps(1, end) infinite;
  border-left: 0px solid transparent;
  border-right: 7px solid transparent;
  border-top: 43px solid yellow;
  box-shadow: yellow -7px -32px;
  content: "";
  display: block;
  height: 0;
  width: 0;
  position: absolute;
  left: 57px;
  top: 70px;
  -webkit-transform: rotate(14deg);
  -ms-transform: rotate(14deg);
  transform: rotate(14deg);
  -webkit-transform-origin: 50% -60px;
  -ms-transform-origin: 50% -60px;
  transform-origin: 50% -60px;
}
@-webkit-keyframes stormy {
  50% {
    -webkit-transform: translateY(-20px);
    transform: translateY(-20px);
  }
}
@keyframes stormy {
  50% {
    -webkit-transform: translateY(-20px);
    transform: translateY(-20px);
  }
}
@-webkit-keyframes stormy_shadow {
  50% {
    -webkit-transform: translateY(20px) scale(1);
    transform: translateY(20px) scale(1);
    opacity: 0.05;
  }
}
@keyframes stormy_shadow {
  50% {
    -webkit-transform: translateY(20px) scale(1);
    transform: translateY(20px) scale(1);
    opacity: 0.05;
  }
}
@-webkit-keyframes stormy_thunder {
  0% {
    -webkit-transform: rotate(20deg);
    transform: rotate(20deg);
    opacity: 1;
  }
  5% {
    -webkit-transform: rotate(-34deg);
    transform: rotate(-34deg);
    opacity: 1;
  }
  10% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
    opacity: 1;
  }
  15% {
    -webkit-transform: rotate(-34deg);
    transform: rotate(-34deg);
    opacity: 0;
  }
}
@keyframes stormy_thunder {
  0% {
    -webkit-transform: rotate(20deg);
    transform: rotate(20deg);
    opacity: 1;
  }
  5% {
    -webkit-transform: rotate(-34deg);
    transform: rotate(-34deg);
    opacity: 1;
  }
  10% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
    opacity: 1;
  }
  15% {
    -webkit-transform: rotate(-34deg);
    transform: rotate(-34deg);
    opacity: 0;
  }
}
</style>
