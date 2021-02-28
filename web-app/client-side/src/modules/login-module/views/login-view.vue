<template>
    <v-app id="inspire" >
        <v-container>
            <v-card  elevation="4"  class="mt-5 mx-a">
                <validation-observer
            ref="observer"
            v-slot="{ invalid }"
            >
                <v-form @submit.prevent="submit" ref="form">

                    <v-card-title>
                        <h1>{{ $t('login') }}</h1>
                    </v-card-title>
                
                    <v-card-text>

                        <validation-provider name="username" rules="required|minmax:2,55"
                            v-slot="{ errors }">

                            <v-text-field 
                                v-model="user.username" 
                                prepend-icon="mdi-account-circle">

                                <template #label>
                                    <span v-t="'username'"></span>
                                </template>

                            </v-text-field>
                            <span class="red--text">{{  errors[0] }}</span>
                        </validation-provider>
                        <validation-provider name="password" rules="required|minmax:2,25"
                            v-slot="{ errors }">

                            <v-text-field 
                                :type="showPassword ? 'text' : 'password'" 
                                v-model="user.password" 
                                prepend-icon="mdi-lock" 
                                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                @click:append="showPassword = !showPassword">
                                <template #label>
                                    <span v-t="'password'"></span>
                                </template>
                            </v-text-field>
                            <span class="red--text">{{  errors[0] }}</span>
                        </validation-provider>
                        <LocaleSwitcher />
                    
                    </v-card-text>
                    <v-divider></v-divider>
                    <v-card-actions>
                        <v-btn type="submit"  :disabled="invalid" color="info">{{ $t('login') }}</v-btn>
                    </v-card-actions>
                </v-form>
                </validation-observer>
                <TheMessage ref="message" />
            </v-card>
        </v-container>
    </v-app>
    
</template>


<style lang="scss">
</style>

<script>
import {ValidationProvider, ValidationObserver}  from 'vee-validate';
import '../../core-module/services/validation-service';
import TheMessage from '../../core-module/components/the-message'
import LocaleSwitcher from '../../core-module/components/the-local-switcher'
import http from '../../core-module/services/http'
import system from '../../core-module/services/system-service'
import router from '../../../router'
import Vue from 'vue'
import homePageMixin from "../../admin-module/mixins/homePageMixin";

export default {
    name: 'LoginView',
    components: {
        ValidationProvider,
        ValidationObserver,
        LocaleSwitcher,
        TheMessage
    },
    mixins: [homePageMixin],
    data(){
        return {
            user:  this.$user,
            showPassword: false
        }
    },
    methods: {
        submit(e) {
            e.preventDefault();
            console.log(this.$refs.observer.validate())
            console.log(localStorage.getItem("user"))
            http.addHeader('X-Auth-Username', this.user.username)
            http.addHeader('X-Auth-Password', this.user.password)
            // this.$refs.form.reset()
            http.post('user/login', this.user.toJson()).then(async response=> {
                system.showMessage(this.$refs.message, "logged-in-success", 'success')
                this.user.setSAMLart(response.data.data)
                this.user.setDetails(response.data.metaInfo.user)
                this.user.setCN(response.data.metaInfo.cn)
                Vue.prototype.$user = this.user;
                localStorage.setItem("user", this.user.toString());

                await this.appendHomeRoutes()
                router.push('home')
                
            }).catch(error=> {
                console.log(error)
                
                console.log(error.response)
                var message= http.handleError(error);
                system.showMessage(this.$refs.message, message, 'error')
                
            });
            

                
        }
    },
    mounted: function() {
        localStorage.removeItem("user")
    }
}
</script>