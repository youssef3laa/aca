<template>
          <v-select
            :items="items"
            label="Outlined style"
            outlined
          ></v-select>
</template>

<script>
export default {
    data() {
        return {
            items: ['Foo', 'Bar', 'Fizz', 'Buzz']
        }
    },
}
</script>