<template>
  <div class="feature__image">
    <div class="schedule__animation">
      <div class="schedule__bar schedule__bar--1"></div>
      <div class="schedule__bar schedule__bar--2"></div>
      <div class="schedule__bar schedule__bar--3"></div>
      <div class="schedule__bar schedule__bar--4"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "schedule",
  methods: {
    handleScroll() {
      const elementsByClassName = document.getElementsByClassName(
        "schedule__bar"
      );
      elementsByClassName.forEach(e => {
        e.style.width = "40%";
        e.style.opacity = 100;
      });
    }
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  }
};
</script>

<style scoped>
.feature__image {
  height: 550px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  background: url(../../assets/svg/brower.svg) no-repeat center;

  background-size: auto 90%;
}

.feature__image {
  overflow: hidden;
  position: relative;
}

.schedule__animation {
  margin-left: auto;
  margin-right: auto;
  width: 536px;
  margin-top: 50px;
}
.schedule__bar {
  /* width: 50%; */
}
.schedule__bar {
  background: #48b7ab;
  height: 24px;
  border-radius: 6px;
  position: relative;
  margin-bottom: 20px;
  width: 0;
  opacity: 0;
  transition: opacity 1000ms, width 2000ms;
  transition-delay: 300ms;
}
.schedule__bar--1 {
  left: 10%;
}
.schedule__bar--2 {
  left: 20%;
  transition-delay: 500ms;
}
.schedule__bar--3 {
  left: 30%;
  transition-delay: 800ms;
}
.schedule__bar--4 {
  left: 50%;
  transition-delay: 1000ms;
}
@media screen and (max-width: 800px) {
  .feature__image {
    background-size: auto 75%;
  }
  .schedule__bar {
    height: 18px;
  }
}

@media screen and (max-width: 548px) {
  .feature__image {
    background-size: auto 45%;
  }
  .schedule__bar {
    height: 15px;
  }
}
</style>
