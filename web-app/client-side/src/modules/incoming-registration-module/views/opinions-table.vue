<template>
    <DataTableComponent :key="field.opinionsTable"
                        :field="field"
                        :val="val">

    </DataTableComponent>
</template>

<script>
import DataTableComponent from "../../application-builder-module/components/dataTable-component";


export default {
    name: "opinions-table",

    components: {DataTableComponent},
    props: ["requestId"],
    data() {
        return {
            val: {},
            field: {
                "key": "opinionsTable",
                "type": "DataTableComponent",
                "name": "opinionsTable",
                "subscribe": "tasks",
                "col": 12
            }
        }
    },
    created() {
        this.val = {
            url: "opinion/" + this.requestId,
            headers: [
                {
                    text: "name",
                    align: "start",
                    value: "displayName",
                },
                {
                    text: "theEntity",
                    value: "unitName",
                },
                {
                    text: "date",
                    value: "opinionDate",
                }
            ],
            subHeaders: [{
                text: "opinion",
                value: "opinion"
            }],
            key:"id",
            data: []
        }
    },
    mounted() {


    }
}
</script>

<style scoped>

</style>
