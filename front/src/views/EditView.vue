<script setup lang="ts">
import {defineProps, ref} from "vue";
import axios from "axios";
import router from "@/router";

const post = ref({
  id : 0,
  title : "",
  content : "",
});

const props = defineProps({
  postId: {
    type : [Number, String],
    require : true,
  }
})


axios.get(`/api/posts/${props.postId}`).then((response)=>{

  post.value = response.data;

});

const edit = () =>{
  axios.patch(`/api/posts/${props.postId}`, post.value).then(()=>{

    router.replace({name: "home"})

  });
}

</script>

<template>

  <div>
    <el-input v-model="post.title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"></el-input>
  </div>

  <div class ="mt-2">
    <el-button type="warning" @click="edit()">글 수정완료</el-button>
  </div>


</template>

<style scoped>

</style>