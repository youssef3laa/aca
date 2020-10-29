<template>

    <v-switch
        v-model="showMessages"
        label="Show messages"
    >
        <template #label>
            <span v-t="'currentLanguage'"></span>
        </template>  
    </v-switch>

</template>

<script>
import { localize } from 'vee-validate';
export default {
    name: 'LocaleSwitcher',
    data() {
        return {
            showMessages: false,
        }
    },
    methods: { 
        switchLocale(value) {
            var locale = 'en'
            if(value) locale = 'ar'
            
            if (this.$i18n.locale !== locale) {
                this.$i18n.locale = locale;
                localize(locale);
                // this.$validator.localize('ar');
                if(this.$i18n.locale == "ar") {
                    this.$vuetify.rtl = true
                }else {
                    this.$vuetify.rtl = false
                }
            }
            
        },
    },
    watch: {
        showMessages (value) {
            this.switchLocale(value)
        },
    },
    created: function () {
        console.log( this.$validator)
        localize(this.$i18n.locale);
        if(this.$i18n.locale == "en"){
            this.$vuetify.rtl = false
            this.showMessages = false;
        }else {
            this.showMessages = true;
            this.$vuetify.rtl = true
        }

    },
}
</script>

