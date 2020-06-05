<template>
  <div>
    <v-card class="mx-auto card" max-width="320" :disabled="isDisabled">
      <p class="text-center card-title">
        {{ title }}
      </p>
      <p class="text-center">
        <v-icon size="70px" :color="iconColors">{{ icon }}</v-icon>
      </p>

      <p class="text-center card-subtitle">
        {{ price }}
      </p>
      <v-card-text class="card-content text-left">
        <ul v-for="(item, index) in content" :key="index">
          <li>
            <div ref="card-{{index}}">{{ item }}</div>
          </li>
        </ul>
      </v-card-text>
      <router-link to="/signup">
        <v-btn class="white--text" block color="purple" :disabled="isDisabled">
          {{ button }}</v-btn
        >
      </router-link>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "priceCard",
  props: [
    "iconColors",
    "icon",
    "title",
    "price",
    "content",
    "button",
    "isDisabled"
  ]
};
</script>

<style lang="scss" scoped>
button {
  @include font-bold;
}
.card {
  padding: 20px;
  min-width: 300px;
  &-title {
    @include font-light;
    font-size: 30px;
    &::before {
      @include before-after(5px);
    }
    &::after {
      @include before-after(10px);
    }
  }
  &-subtitle {
    @include font-bold;
    font-size: 25px;
    &::after {
      @include before-after(30px);
    }
  }
  &-content {
    font-size: 16px;
    &::after {
      @include before-after(150px);
    }
    @include font;
    color: grey;
  }
  li {
    padding: 5px;
  }
  @media screen and (max-height: 768px) {
    &-content {
      font-size: 16px;
      &::after {
        @include before-after(100px);
      }
    }
  }
}
</style>
