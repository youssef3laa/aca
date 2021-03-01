<template>
    <v-row>
        <v-col v-for="(input, index) in inputs" :key="index" :cols="4" :md="4">
            <AutocompleteComponent
                    :field="input.field"
                    :val="input.val"
                    @update="onChange($event,input,index)"></AutocompleteComponent>
        </v-col>
    </v-row>
</template>

<script>
import AutocompleteComponent from './autocomplete-component'
import userMixin from "../../../mixins/userMixin"
import orgChartMixin from "../../../mixins/orgChartMixin";

export default {
        components: {AutocompleteComponent},
        mixins: [userMixin,orgChartMixin],
        props: ["val","field"],
        data() {
            return {
                d: this.val,
                firstTime: true,
                userDetails: null,
                parent: null,
                code: null,
                assignedCN: null,
                assignedRole: null,
                values: [],
                inputs: []
            }
        },
        methods: {
            onValueChange: async function() {
                await this.handleReceiverFormOutput()
                this.$emit('update', {
                    name: this.field.name,
                    value: {
                        values: this.values,
                        assignedCN: this.assignedCN,
                        code: this.code,
                        role: this.assignedRole
                    }
                })
            },
            handleReceiverFormOutput: async function () {
                this.assignedCN = null
                this.code = null
                this.role = null

                let valuesLast = this.values.length - 1
                if(valuesLast < 0) return
                if(this.values[valuesLast].unitCode) {
                    this.code = this.values[valuesLast].unitTypeCode
                    this.assignedRole = await this.getHeadRoleByUnitCode(this.values[valuesLast].unitCode)
                    this.assignedCN = this.assignedRole.cn
                } else if(this.values[valuesLast].groupCode) {
                    this.code = this.values[valuesLast].groupCode
                    this.assignedRole = this.values[valuesLast]
                    this.assignedCN = this.assignedRole.cn
                }
            },
            onChange: async function(event, input, index){
                console.log("Event", event)
                console.log("Input", input)
                console.log("Index", index)
                if(event.value.value){
                    if(!input.checkSelf || (input.checkSelf && this.checkCanSelectNext(input.type,event.value.value.value))){
                        this.values.push(event.value.value.object)
                        this.fillNextInput(index, event.value.value.value)
                    }
                } else{
                    this.emptyNext(index)
                }
                this.onValueChange()
            },
            checkCanSelectNext: function(type,code) {
                if(type == "unit"){
                    return (this.userDetails.groups[0].unit.unitCode == code)
                }else if(type == "role"){
                    return (this.userDetails.groups[0].groupCode == code)
                }else{
                    return true;
                }
            },
            initializeInputs: function(inputs){
                if(!(inputs instanceof Array)) inputs = [inputs]
                for(let i = 0 ; i < inputs.length ; i++){
                    if(i == 0){
                        let url = this.fillURL(inputs[i].url)
                        inputs[i].val = this.getAutocompleteVal(url)
                    }else{
                        inputs[i].val = this.getAutocompleteVal(null)
                    }
                    inputs[i].field = this.getAutocompleteField(inputs[i].name)
                }
                return inputs
            },
            getAutocompleteField: function(name) {
                return {
                    name: name,
                    autofill: true
                }
            },
            getAutocompleteVal: function(url) {
                return {
                    url : url
                }
            },
            fillNextInput: function(index, code) {
                if(index < this.inputs.length-1){
                    let url = this.fillURL(this.inputs[index+1].url,code)
                    this.inputs[index+1].val = this.getAutocompleteVal(url)
                    this.restartInputs()
                }
            },
            fillURL: function(url, code){
                url = url.replace(/\$user/, this.userDetails.userId)
                url = url.replace(/\$group/, this.userDetails.groups[0].groupCode)
                url = url.replace(/\$unit/, this.userDetails.groups[0].unit.unitCode)

                url = url.replace(/\$parentGroup/, this.parent?.groupCode)
                url = url.replace(/\$parentUnit/, this.parent?.unit?.unitCode)

                url = url.replace(/\$\d+/,code)
                return url
            },
            emptyNext: function(index) {
                this.values.splice(index)
                for(let i = index+1 ; i < this.inputs.length ; i++){
                    this.inputs[i].val = this.getAutocompleteVal(null)
                }
                this.restartInputs()
            },
            restartInputs: function () {
                let temp = this.inputs
                this.inputs = []
                this.inputs = temp
            }
        },
        watch: {
            val: function (newVal) {
                if(newVal.inputs){
                    this.inputs = this.initializeInputs(newVal.inputs)
                    this.onValueChange()
                }

                for(let key in newVal){
                    this.d[key] = newVal[key]
                }
            }
        },
        async mounted() {
            this.userDetails = await this.getUserDetails()
            this.parent = await this.getParentDetails()
            if(this.val.inputs){
                this.inputs = this.initializeInputs(this.val.inputs)
            }
            this.onValueChange()
        }
    }
</script>
