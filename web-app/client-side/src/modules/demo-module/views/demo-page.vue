<template>
    <div>
        <TheNavbar />
        <v-app>
            <v-container>
                <v-row>
                    <v-col>
                        <v-file-input
                            show-size
                            outlined
                            counter
                            label="Input File" v-model="file"
                        ></v-file-input>
                    </v-col>
                    <v-col>
                        <v-btn @click="upload">Upload</v-btn>
                    </v-col>
                </v-row>
            </v-container>
        </v-app>    
    </div>
</template>

<script>
import TheNavbar from '../../core-module/components/the-nav-bar'

export default {
    name: 'DemoPage',
    data() {
        return {
            file: null
        }
    },
    components: {
        TheNavbar,
    },
    methods: {
        upload () {
            console.log(this.file);
        }
    }
}
</script>