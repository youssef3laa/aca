<template>
    <span>
        <v-row>
            <v-col :cols="8" :md="8">
                <AutoCompleteComponent :field="unit.field"
                                       :val="unit.val"
                                       @update="onChangeUnit"></AutoCompleteComponent>
            </v-col>
            <v-col>
                <v-btn text color="#07689F" style="background-color: #f2f7fa" height="52px" @click="addUnit">
                    <v-icon style="font-size: medium">fas fa-layer-group</v-icon>
                    <span style="margin: 3px"></span>
                    <span style="font-weight: bold" v-t="'addUnit'"></span>
                </v-btn>
            </v-col>
        </v-row>
        <v-row >
            <v-col>
                <DataTableComponent :field="table.field" :val="table.val"></DataTableComponent>
            </v-col>
        </v-row>
        <AlertComponent ref="alertComponent"></AlertComponent>
    </span>
</template>

<script>
    import AutoCompleteComponent from "./autocomplete-component";
    import DataTableComponent from "./dataTable-component";
    import orgChartMixin from "../../../mixins/orgChartMixin";
    export default {
        name: 'multipleAssigneeComponent',
        components: {DataTableComponent, AutoCompleteComponent},
        mixins: [orgChartMixin],
        props: ['val', 'field'],
        data() {
            return {
                d: this.val,
                unit: {
                    field: {
                        name: 'theUnit'
                    },
                    val: {
                        url : 'org/unit/read/list'
                    }
                },
                unitSelected: null,
                table: {
                    field: {
                        name: 'theUnits',
                        actions: [{name: 'assign', icon:'fas fa-exchange-alt'},'delete']
                    },
                    val: {headers: [], data: []}
                },
                tableData: [],
                assignees: { assignee: [] },
                nextAssignee: null
            }
        },
        methods: {
            onValueChange: function() {
                this.$emit('update', {
                    name: this.field.name,
                    value: {
                        units: this.tableData,
                        assignees: this.assignees,
                        nextAssignee: this.nextAssignee
                    }
                })
            },
            onChangeUnit: function(event){
                if(event.value.value){
                    this.unitSelected = event.value.value.object
                }else{
                    this.unitSelected = null
                }
            },
            addUnit: async function () {
                if((!this.unitSelected) || (this.getUnitIndex(this.unitSelected) != -1)) return
                let role = await this.getHeadRoleByUnitCode(this.unitSelected.unitCode)
                this.tableData.push({unit: this.unitSelected, role: role})
                if(this.tableData.length == 1){
                    this.nextAssignee = {
                        cn : role.cn,
                        code : role.groupCode,
                        role : role
                    }
                }else{
                    this.assignees.assignee.push(role.cn)
                }
                this.onValueChange()
            },
            deleteUnit: function(object){
                if(this.nextAssignee && object.item.role.cn == this.nextAssignee.cn) this.nextAssignee = null
                if(this.getUnitIndex(object.item.unit) > -1)this.tableData.splice(this.getUnitIndex(object.item.unit),1)
                if(this.getAssigneeCNIndex(object.item.role.cn) > -1) this.assignees.assignee.splice(this.getAssigneeCNIndex(object.item.role.cn),1)
                this.updateTableVal(this.tableData)
                this.onValueChange()
            },
            assignUnit: function(object){
                if(this.nextAssignee && this.nextAssignee.cn) this.assignees.assignee.push(this.nextAssignee.cn)
                this.nextAssignee = {
                    cn : object.item.role.cn,
                    code : object.item.role.groupCode,
                    role : object.item.role
                }
                if(this.getAssigneeCNIndex(object.item.role.cn) > -1) this.assignees.assignee.splice(this.getAssigneeCNIndex(object.item.role.cn),1)
                this.$refs.alertComponent._alertSuccess({message:"assignedSuccess"})
                this.onValueChange()
            },
            getAssigneeCNIndex: function(cn){
                for(let element in this.assignees.assignee){
                    if(this.assignees.assignee[element] == cn) return parseInt(element)
                }
                return -1
            },
            getUnitIndex: function(unit){
                for(let element in this.tableData){
                    if(this.tableData[element].unit.unitCode == unit.unitCode) return parseInt(element)
                }
                return -1
            },
            updateTableVal: function(data) {
                this.table.val = {
                    headers: [
                        {
                            text: "الجهة",
                            align: "start",
                            value: "unit.nameAr",
                        },
                        {
                            text: "رئيس الجهة",
                            value: "role.nameAr",
                        },
                        {
                            text: "كود الجهة",
                            value: "unit.unitCode",
                        },
                        {
                            text: "مستوى الجهة",
                            value: "unit.unitTypeCode",
                        },
                        {
                            text: "",
                            value: "action",
                        }
                    ],
                    data: data
                }
            }
        },
        watch: {
            val: function(newVal) {
                for(var key in  newVal){
                    this.d[key] = newVal[key]
                }
            },
            tableData: function (newVal){
                this.updateTableVal(newVal)
            }
        },
        created() {
            this.$observable.subscribe("theUnits_delete", this.deleteUnit)
            this.$observable.subscribe("theUnits_assign", this.assignUnit)
        },
        mounted() {
            this.updateTableVal([])
        },
        destroyed() {
            this.$observable.unsubscribe("theUnits_delete", this.deleteUnit)
            this.$observable.unsubscribe("theUnits_assign", this.assignUnit)
        }
    }
</script>