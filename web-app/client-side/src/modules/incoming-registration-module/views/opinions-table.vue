<template>
    <DataTableComponent :key="dataTableField.key"
                        :field="dataTableField"
                        :val="val">

    </DataTableComponent>
</template>

<script>
import DataTableComponent from "../../application-builder-module/components/dataTable-component";


export default {
    name: "opinions-table",

    components: {DataTableComponent},
    props: ["field", "val"],
    data() {
        return {
            dataTableField: {}

        }
    },
    created() {
        this.dataTableField = {
            "type": "DataTableComponent",
            "key": "id",
            "name": "opinionsTableComponent",
            "col": 12
        }
    },
    mounted() {

    }
}
</script>

<style scoped>

</style>
