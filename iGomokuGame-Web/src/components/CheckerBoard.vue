<script setup>
import { ref } from 'vue'
const width = ref(500)
const height = ref(500)..
</script>

<template>
    <svg class="checkerBoard" :width="width" :height="height">
        <defs>
            <rect id="mark" x="-1" y="-1" width="2" height="2" />
        </defs>
        <path stroke="brown" stroke-width="0.5" fill="none"
            d="m-70,-70h140v140h-140zm10,0v140m10,0v-140m10,0v140m10,0v-140m10,0v140m10,0v-140m10,0v140m10,0v-140m10,0v140m10,0v-140m10,0v140m10,0v-140m10,0v140m10,-10h-140m0,-10h140m0,-10h-140m0,-10h140m0,-10h-140m0,-10h140m0,-10h-140m0,-10h140m0,-10h-140m0,-10h140m0,-10h-140m0,-10h140m0,-10h-140" />
        <use xlink:href="#mark" />
        <use xlink:href="#mark" x="40" y="40" />
        <use xlink:href="#mark" x="40" y="-40" />
        <use xlink:href="#mark" x="-40" y="40" />
        <use xlink:href="#mark" x="-40" y="-40" />
    </svg>
</template>

<style scoped>
.checkerBoard {
    height: 100vh;
    margin: 0;
    background: bisque;
    border: 1px red solid;
}
</style>
