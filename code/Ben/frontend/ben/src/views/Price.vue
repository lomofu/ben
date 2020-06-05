<template>
  <div class="price">
    <index-nav></index-nav>
    <div class="price-main">
      <div class="price-box text-center d-flex flex-column ">
        <p class="price-title animated fadeInDown">{{ config.title }}</p>
        <v-img src="../../public/img/price/price.png" height="200px" contain />
      </div>
      <v-row class="price-content d-flex justify-center animated fadeInUp ">
        <div
          v-for="(item, index) in config.card"
          :key="index"
          align="center"
          style="padding: 20px"
        >
          <card
            :title="item.title"
            :price="item.price"
            :content="item.content"
            :iconColors="item.icon.colors"
            :icon="item.icon.data"
            :button="item.button"
            :class="item.className"
            :isDisabled="item.isDisabled"
          ></card>
        </div>
      </v-row>
      <v-row class="price-question mt-12">
        <v-col class="col-12">
          <p class="price-question-title  mb-6 text-center">
            {{ config.questions.title }}
          </p>
          <v-row>
            <v-col
              v-for="(item, index) in config.questions.question"
              :key="index"
              cols="12"
              xs="12"
              sm="12"
              md="6"
              lg="6"
              xl="6"
              class="mt-5"
            >
              <div class="d-flex  justify-center text-left">
                <div>
                  <div class="question-title mb-3">{{ item.title }}</div>
                  <p class="question-answer">{{ item.answer }}</p>
                </div>
              </div>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </div>
    <indexFooter></indexFooter>
  </div>
</template>

<script>
import { PRICE } from "../common/view/Constant";
export default {
  name: "Price",
  components: {
    indexNav: () => import("../components/public/nav.vue"),
    card: () => import("../components/price/priceCard.vue"),
    indexFooter: () => import("../components/public/footer.vue")
  },
  data: () => ({
    config: {
      title: PRICE.VIEW.TITLE,
      card: PRICE.VIEW.CARD,
      questions: PRICE.VIEW.QUESTIONS
    }
  })
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/price/price.scss";
</style>
