<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({
  postId: {
    type : [Number, String],
    require : true,
  }
})

const post = ref({
  id : 0,
  title : "",
  content : "",
});

const router = useRouter();

const moveToEdit = () =>{
  router.push({name : "edit",params : {postId : props.postId}});
}

onMounted(()=>{

  axios.get(`/api/posts/${props.postId}`).then((response)=>{

    post.value = response.data;

  });
})

</script>

<template>
  <h2>{{ post.title }}</h2>
  <h2>{{post.content}}</h2>

  <el-button type="warning" @click="moveToEdit()">수정</el-button>
</template>

<style scoped>

</style>