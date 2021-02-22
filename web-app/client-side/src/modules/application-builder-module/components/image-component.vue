<template>
<v-container>
<v-img
:src="val.url"
>
</v-img>
</v-container>
</template>

<script>

export default {
    name:"ImageComponent",
props:['val','field'],
}
</script>

<style>

</style>