<template>

    <v-alert  v-if="show"
        text
        dense
        :type="type"
      >
        {{$t(message)}}
      </v-alert>

</template>

<script>


export default {
    name: 'TheMessage',
    data() {
        return {
            show: false,
            message: "",
            type: "info"
        }
    },
    methods: {
        showMessage(type, message){
            this.show = true;
            this.type = type;
            this.message = message;
            setTimeout(() => {
                this.show = false
            }, 5000);
        }
    },
    
}
</script>

