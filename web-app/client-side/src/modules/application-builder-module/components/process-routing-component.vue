<template>
    <div>
        <v-card outlined>
            <v-alert outlined type="info" prominent icon="fas fa-scroll" style="padding-bottom: 5px">
                <p style="font-size: 16px; color: black">
                    <span style="font-size: 20px; color: #07689F"> ملاحظات </span>
                    <br/>
                    تستخدم هذه المساحة لكتابة ملاحظات
                </p>
            </v-alert>
            <v-card-text style="padding-top: 0px">
                <TextareaComponent :field="{ name: 'routingNotes', label: 'notes' }"
                                   @update="onChangeComment"></TextareaComponent>
            </v-card-text>
        </v-card>
        <div style="padding: 10px"></div>
        <v-card outlined v-if="d.receiverTypes || d.decisionTypes">
            <v-card-title style="color: #07689F" v-if="d.decisionTypes">
                <span v-t="decisionLabel"></span>
                <v-radio-group v-model="decision" row>
                    <v-radio v-for="(type, index) in d.decisionTypes"
                             :key="index"
                             :value="type"
                             style="font-size: 16px; padding: 0px 50px 0px 50px; font-weight: bold">
                        <template #label>
                            <span v-t="getDecisionTypeLabel(type)"></span>
                        </template>
                    </v-radio>
                </v-radio-group>
            </v-card-title>
            <v-divider v-if="showDivider"></v-divider>
            <v-card-title style="color: #07689F" v-if="showReceiver">
                <span v-t="receiverLabel"></span>
                <v-radio-group v-model="receiver" row>
                    <v-radio v-for="(type, index) in d.receiverTypes"
                             :key="index"
                             :value="type"
                             style="font-size: 16px; padding: 0px 50px 0px 50px; font-weight: bold">
                        <template #label>
                            <span v-t="getReceiverTypeLabel(type)"></span>
                        </template>
                    </v-radio>
                </v-radio-group>
            </v-card-title>
            <v-card-text v-if="showReceiver && isSingle">
                <ReceiverFormComponent :field="{name: 'receiverForm'}" :val="null" @update="onChangeReceiver"></ReceiverFormComponent>
            </v-card-text>
            <v-card-text v-else-if="showReceiver && !isSingle">

            </v-card-text>
        </v-card>
    </div>
</template>

<script>
    import TextareaComponent from "./textarea-component"
    import ReceiverFormComponent from "./receiver-form-component"

    export default {
        components: {ReceiverFormComponent, TextareaComponent},
        props: ["val","field"],
        data() {
            return{
                decision: "comment",
                comment: null,
                receiver: null,
                assignee: null,
                assignees: null,
                receiverLabel: "receiver",
                decisionLabel: "decision",
                d: this.val,
                userDetails: null,
                showDivider: false,
                showReceiver: false,
                isSingle: false,
                firstLoad: true
            }
        },
        watch: {
            val: function (newVal) {
                for(var key in newVal){
                    this.d[key] = newVal[key]
                }
                if(this.firstLoad){
                    this.handleDefaultValues()
                    this.firstLoad = false
                }
                console.log("Val Watch", this.d)
            },
            decision: function (newVal) {
                console.log("DecisionType", newVal)
                this.handleShowReceiver()
                this.onValueChange()
            },
            receiver: function (newVal) {
                console.log("ReceiverType", newVal)
                this.handleIsSingle()
                this.onValueChange()
            },
            showReceiver: function () {
                this.handleDivider()
            }
        },
        methods: {
            onValueChange: function() {
                this.$emit('update', {
                    name: this.field.name,
                    value: {
                        decision: this.decision,
                        comment: this.comment,
                        receiver: this.receiver
                    },
                })
            },
            onChangeComment: function(event) {
                this.comment = event.value
                this.onValueChange()
            },
            onChangeReceiver: function(event) {
                console.log("ReceiverForm", event)
            },
            handleDefaultValues: function() {
                if(this.d.decisionTypes instanceof Array && this.d.decisionTypes.length > 0){
                    this.decision = this.d.decisionTypes[0]
                }
                if(this.d.receiverTypes instanceof Array && this.d.receiverTypes.length > 0){
                    this.receiver = this.d.receiverTypes[0]
                }
            },
            handleIsSingle: function() {
                if(this.receiver == "single"){
                    this.isSingle = true
                }else {
                    this.isSingle = false
                }
            },
            handleDivider: function() {
                if(this.d.decisionTypes){
                    if(this.showReceiver){
                        this.showDivider = true
                        return
                    }
                }
                this.showDivider = false
            },
            handleShowReceiver: function() {
                if(this.d.receiverTypes){
                    if(this.d.decisionTypes){
                        if(this.decision == "approve"){
                            this.showReceiver = true
                            return
                        }
                    }else{
                        this.showReceiver = true
                        return
                    }
                }
                this.showReceiver = false
            },
            getDecisionTypeLabel: function(value) {
                switch (value) {
                    case "approve":
                        return "approve-request"
                    case "fulfillment":
                        return "request-fulfillment"
                    case "requestModification":
                        return "request-modification"
                }
            },
            getReceiverTypeLabel: function(value) {
                switch (value) {
                    case "single":
                        return "unit"
                    case "multiple":
                        return "multiple-units"
                }
            },
        },
        async mounted() {
            this.handleDefaultValues()
        }
    }
</script>

<style></style>