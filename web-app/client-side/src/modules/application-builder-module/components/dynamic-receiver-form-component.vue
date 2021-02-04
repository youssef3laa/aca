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

                let group = null
                let code = null
                if((this.higher ||  this.userDetails.groups[0].unit.unitTypeCode== "AGN") && this.direction == "up") {
                    group = this.parent
                    code = this.parent.groupCode
                } else if(this.memberSelected){
                    group = this.memberSelected
                    code = group.groupCode
                }else if(this.groupSelected){
                    group = await this.getHeadRoleByUnitCode(this.groupSelected.unitCode)
                    code = this.groupSelected.unitTypeCode
                }else if(this.officeSelected){
                    group = await this.getHeadRoleByUnitCode(this.officeSelected.unitCode)
                    code = this.officeSelected.unitTypeCode
                }else if(this.sectorSelected){
                    group = await this.getHeadRoleByUnitCode(this.sectorSelected.unitCode)
                    code = this.sectorSelected.unitTypeCode
                }else if(this.agencySelected){
                    group = await this.getHeadRoleByUnitCode(this.agencySelected.unitCode)
                    code = this.agencySelected.unitTypeCode
                }
                this.assignedRole = group
                this.assignedCN = (group)? group.cn:null
                this.code = code
            },
            onChange: async function(event, input, index){
                // if(this.field.readonly || this.agency.field.readonly) return
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
                // this.onValueChange()
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
                    inputs[i].field = this.getAutocompleteField(inputs[i].name,false)
                }
                return inputs
            },
            getAutocompleteField: function(name, readonly) {
                return {
                    name: name,
                    readonly: (this.field.readonly) ? true : readonly,
                    autofill: true
                }
            },
            getAutocompleteVal: function(url) {
                return {
                    url : url
                }
            },
            fillAutocomplete: function(name) {
                return {
                    list: [{ text: name , value: name }],
                    value: name
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
                for(let key in newVal){
                    this.d[key] = newVal[key]
                }
                if(this.field.readonly){
                    this.fillComponent()
                }

                if(this.firstTime){
                    this.firstTime = false
                    if(newVal.inputs){
                        this.inputs = this.initializeInputs(newVal.inputs)
                    }
                    this.onValueChange()
                }
            }
        },
        async mounted() {
            if(!this.field.readonly){
                this.userDetails = await this.getUserDetails()
                if(this.val.inputs){
                    this.inputs = this.initializeInputs(this.val.inputs)
                }
                // this.handleReadOnlyReceiver(this.userDetails.groups[0].unit.unitCode,
                //     this.userDetails.groups[0].unit.unitTypeCode,
                //     this.userDetails.groups[0].isHeadRole)
                await this.onValueChange()
            }
        }
    }
</script>

<!--{-->
<!--    "name": "",-->
<!--    "url": "get/list/ACA/",-->
<!--    "checkSelf": true,-->
<!--    "type": "unit" // role-->
<!--},-->
<!--{-->
<!--    "label": "",-->
<!--    "url": "get/list/$1"-->
<!--}-->