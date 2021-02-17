<template>
    <div>
        <v-row v-if="d.fields">
            <v-col v-for="(field, index) in d.fields" :key="index" :cols="(d.fields.length>1)? 6:0">
                <v-card outlined>
                    <v-alert outlined type="info" prominent :icon="field.icon" style="padding-bottom: 5px">
                        <p style="font-size: 16px; color: black">
                            <span style="font-size: 20px; color: #07689F" v-t="field.title"></span>
                            <br/>
                            <span v-t="field.text"></span> {{displayName}}
                        </p>
                    </v-alert>
                    <v-card-text style="padding-top: 0px">
                        <TextareaComponent :field="{ name: field.name, label: field.name }"
                                           @update="onChangeField($event,field)"></TextareaComponent>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
        <div style="padding: 10px" v-if="d.fields && (decisions || receiverTypes)"></div>
        <v-card outlined v-if="decisions || receiverTypes">
            <RadioGroupComponent :field="{ name: 'decision', title: 'decision' }"
                                 :val="decisions"
                                 @update="onChangeDecision"
                                 v-if="decisions"></RadioGroupComponent>
            <v-divider v-if="decisions && receiverTypes"></v-divider>
            <RadioGroupComponent :field="{ name: 'receiver', title: 'receiver' }"
                                 :val="receiverTypes"
                                 @update="onChangeReceiverType"
                                 v-if="receiverTypes"></RadioGroupComponent>
            <v-card-text v-if="receiverTypes && receiverType=='single'">
                <ReceiverFormDynamicComponent :field="{name: 'receiverForm'}" :val="receiverInputs" @update="onChangeReceiver"></ReceiverFormDynamicComponent>
            </v-card-text>
            <v-card-text v-else-if="receiverTypes && receiverType=='multiple'">
                <MultipleAssigneeComponent :field="{name: 'multipleComponent'}" @update="onChangeMultipleUnits"></MultipleAssigneeComponent>
            </v-card-text>
        </v-card>
    </div>
</template>

<script>
    import TextareaComponent from "./textarea-component"
    import RadioGroupComponent from "./radio-group-component";
    import userMixin from "../../../mixins/userMixin";
    import MultipleAssigneeComponent from "./multiple-assignee-component";
    import orgChartMixin from "../../../mixins/orgChartMixin";
    import ReceiverFormDynamicComponent from "./receiver-form-dynamic-component";

    export default {
        components: {
            ReceiverFormDynamicComponent,
            MultipleAssigneeComponent, RadioGroupComponent, TextareaComponent},
        props: ["val","field"],
        mixins: [userMixin,orgChartMixin],
        data() {
            return{
                d: this.val,
                inputs: {},
                firstTime: true,
                userDetails: null,
                displayName: null,
                parent: null,

                decision: "send",
                decisions: null,

                receiverType: null,
                receiverTypes: null,
                receiverInputs: null,

                receiver: {}
            }
        },
        methods: {
            onValueChange: function() {
                // if(!this.assignedCN && this.parent && this.decision == "comment") {
                //     this.assignedCN = this.parent.cn
                //     this.code = this.parent.groupCode
                //     this.assignedRole = this.parent
                // }
                if(this.receiverType) this.receiver.type = this.receiverType
                console.log("Approval Card", {
                    decision: this.decision,
                    inputs: this.inputs,
                    selected: this.receiver
                })
                this.$emit('update', {
                    name: this.field.name,
                    value: {
                        decision: this.decision,
                        inputs: this.inputs,
                        selected: this.receiver
                    }
                })
            },
            onChangeField: function(event, field) {
                this.inputs[field.name] = event.value
                console.log("Fields",this.fields)
                this.onValueChange()
            },
            onChangeMultipleUnits: function(event){
                if(event.value.assignees) this.receiver.assignees = event.value.assignees
                if(event.value.nextAssignee){
                    this.receiver.assignedCN = event.value.nextAssignee.cn
                    this.receiver.code = event.value.nextAssignee.code
                    this.receiver.role = event.value.nextAssignee.role
                }
                this.onValueChange()
            },
            onChangeReceiver: async function(event) {
                console.log("ReceiverForm", event)
                this.receiver = event.value
                this.onValueChange()
            },
            onChangeDecision: function(event) {
                this.receiver = {}
                this.receiverTypes = undefined
                this.receiverInputs = undefined
                this.decision = event.value
                for(let key in this.d.decisions){
                    if(this.d["decisions"][key].name == this.decision){
                        this.receiverTypes = this.getReceiverTypeOptions(this.d["decisions"][key].types)
                        this.receiverInputs = {inputs:this.d["decisions"][key].inputs}
                        break
                    }
                }
                this.onValueChange()
            },
            onChangeReceiverType: function(event) {
                this.receiver = {}
                this.receiverType = event.value
                this.onValueChange()
            },
            getDecisionOptions: function(decisions) {
                if(!decisions) return null
                if(!(decisions instanceof Array)) decisions = [decisions]
                let options = [];
                for(let i in decisions){
                    options.push({
                        name: decisions[i].name,
                        label: decisions[i].label
                    })
                }
                if(options.length == 0) return null
                return {options: options}
            },
            getReceiverTypeOptions: function(receiverTypes) {
                if(!receiverTypes) {
                    this.receiverType = null
                    return null
                }
                if(!(receiverTypes instanceof Array)) receiverTypes = [receiverTypes]
                let options = [];
                for(let i in receiverTypes){
                    options.push({
                        name: receiverTypes[i],
                        label: this.getReceiverTypeLabel(receiverTypes[i])
                    })
                }
                this.receiverType = receiverTypes[0]
                return {options: options, value: receiverTypes[0]}
            },
            getReceiverTypeLabel: function(value) {
                switch (value) {
                    case "single":
                        return "unit"
                    case "multiple":
                        return "multiple-units"
                }
            }
        },
        watch: {
            val: function (newVal) {
                if(newVal.fields){
                    if(!(newVal.fields instanceof Array)) newVal.fields = [newVal.fields]
                    this.inputs = {}
                }
                if(newVal.decisions){
                    this.decisions = this.getDecisionOptions(newVal.decisions)
                }
                if(newVal.receiver){
                    this.receiverTypes = (newVal.decisions)? null:this.getReceiverTypeOptions(newVal.receiver.types)
                    this.receiverInputs = (newVal.decisions)? null:{inputs:newVal.receiver.inputs}
                }

                if(this.firstTime){
                    if(newVal.fields || newVal.decisions || newVal.receiverTypes){
                        this.d = newVal
                    }
                    this.firstTime=false
                    this.onValueChange()
                }else{
                    for(let key in newVal){
                        this.d[key] = newVal[key]
                    }
                }
            }
        },
        async created() {
            this.decisions = this.getDecisionOptions(this.val.decisions)
            if(this.val.receiver){
                this.receiverTypes = (this.val.decisions)? null:this.getReceiverTypeOptions(this.val.receiver.types)
                this.receiverInputs = (this.val.decisions)? null:{inputs:this.val.receiver.inputs}
            }

            this.userDetails = await this.getUserDetails()
            this.displayName = this.userDetails.displayName
            this.parent = await this.getParentDetails()

            this.onValueChange()
        }
    }
</script>