<template>
    <div>
        <v-data-table
                :headers="d.headers"
                :items="d.data"
                :options.sync="options"
                :server-items-length="totalItems"
                :loading="loading"
                :footer-props="footerProps"
                class="elevation-1"
        ></v-data-table>
    </div>
</template>
<script>
    import http from "../../core-module/services/http";
    export default {
        name: 'DataTableComponent',
        data() {
            return {
                totalItems: 0,
                d: this.val,
                loading: true,
                footerProps: {
                    'items-per-page-options': [5,10,25,-1]
                },
                options: {},
            };
        },
        watch: {
            options: {
                handler(event) {
                    this.getDataFromApi(event);
                },
                deep: true,
            },
            val: function (newVal) {
                for(var key in newVal){
                    this.d[key] = newVal[key];
                }
                if(this.d.url){
                    this.getDataFromApi({ page: 1, itemsPerPage: 10 })
                }
            },
        },
        methods: {
            getDataFromApi(event) {
                if(!this.d.url)  return;

                this.loading = true;
                const page = event.page - 1;
                let limit = event.itemsPerPage;
                if(limit == -1){
                    limit = this.totalItems;
                }
                const URL = this.d.url + "?pageNumber="+page+"&pageSize="+limit;
                http.get(URL).then((response) => {
                    this.totalItems = response.data.data.count;
                    this.d.data = response.data.data.list;
                    this.loading = false;
                }).catch((error) => {
                    console.error(error);
                });
            },
        },
        created() {
            if (this.field.subscribe) {
                console.log("subscribe");
                this.$observable.subscribe(this.field.subscribe, (data) => {
                    if (data.type == "modelUpdate") {
                        var keys = Object.keys(data.model);
                        keys.forEach((key, index) => {
                            console.log(index);
                            this.d[key] = data.model[key];
                        });
                    }
                    console.log(data);
                });
            }
        },
        props: ["val", "field"],
    };
</script>