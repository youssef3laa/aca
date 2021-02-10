<template>
  <div>
    <v-row v-if="d.fields">
      <v-col v-if="d.fields && d.fields.includes('comment')">
        <v-card outlined>
          <v-alert outlined type="info" prominent icon="fas fa-scroll" style="padding-bottom: 5px">
            <p style="font-size: 16px; color: black">
              <span style="font-size: 20px; color: #07689F" v-t="'notes'"></span>
              <br/>
              <span v-t="'this-field-for-notes'"></span> {{displayName}}
            </p>
          </v-alert>
          <v-card-text style="padding-top: 0px">
            <TextareaComponent :field="{ name: 'routingNotes', label: 'notes', rule:'required' }"
                               @update="onChangeComment"></TextareaComponent>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col v-if="d.fields && d.fields.includes('opinion')">
        <v-card outlined>
          <v-alert outlined type="info" prominent icon="far fa-file-alt" style="padding-bottom: 5px">
            <p style="font-size: 16px; color: black">
              <span style="font-size: 20px; color: #07689F" v-t="'express-opinion'"></span>
              <br/>
              <span v-t="'this-field-for-opinion'"></span> {{displayName}}
            </p>
          </v-alert>
          <v-card-text style="padding-top: 0px">
            <TextareaComponent :field="{ name: 'routingOpinion', label: 'opinion', rule:'required' }"
                               @update="onChangeOpinion"></TextareaComponent>
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
        <ReceiverFormComponent :key="receiverType" :field="{name: 'receiverForm'}" :val="receiverDirection" @update="onChangeReceiver"></ReceiverFormComponent>
      </v-card-text>
      <v-card-text v-else-if="receiverTypes && receiverType=='multiple'">
        <MultipleAssigneeComponent :key="receiverType" :field="{name: 'multipleComponent'}" @update="onChangeMultipleUnits"></MultipleAssigneeComponent>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
  import TextareaComponent from "./textarea-component"
  import ReceiverFormComponent from "./receiver-form-component"
  import RadioGroupComponent from "./radio-group-component";
  import userMixin from "../../../mixins/userMixin";
  import MultipleAssigneeComponent from "./multiple-assignee-component";
  import orgChartMixin from "../../../mixins/orgChartMixin";

  export default {
    components: {MultipleAssigneeComponent, RadioGroupComponent, ReceiverFormComponent, TextareaComponent},
    props: ["val","field"],
    mixins: [userMixin,orgChartMixin],
    data() {
      return{
        d: this.val,
        firstTime: true,
        displayName: null,
        userDetails: null,
        parent: null,
        comment: null,
        opinion: null,
        decision: "comment",
        receiver: null,
        assignedRole: null,
        assignee: null,
        assignees: null,
        code: null,
        receiverType: null,
        decisions: null,
        receiverTypes: null,
        receiverDirection: null,
        direction: null,
        minimumLevel: null
      }
    },
    methods: {
      onValueChange: function() {
        if(!this.assignedCN && this.parent && this.decision == "comment") {
          this.assignedCN = this.parent.cn
          this.code = this.parent.groupCode
          this.assignedRole = this.parent
        }
        this.$emit('update', {
          name: this.field.name,
          value: {
            decision: this.decision,
            comment: this.comment,
            opinion: this.opinion,
            receiverType: (this.receiverType)? this.receiverType:'single',
            code: this.code,
            role: this.assignedRole,
            assignedCN: this.assignee,
            assignees: this.assignees
          }
        })
      },
      onChangeComment: function(event) {
        this.comment = event.value
        this.onValueChange()
      },
      onChangeOpinion: function(event) {
        this.opinion = event.value
        this.onValueChange()
      },
      onChangeMultipleUnits: function(event){
        this.assignees = event.value.assignees
        if(event.value.nextAssignee){
          this.assignee = event.value.nextAssignee.cn
          this.code = event.value.nextAssignee.code
          this.assignedRole = event.value.nextAssignee.role
        }else{
          this.assignee = null
          this.code = null
          this.assignedRole = null
        }
        this.onValueChange()
      },
      onChangeReceiver: async function(event) {
        this.assignees = null
        console.log("ReceiverForm", event)
        this.receiver = event
        this.assignee = event.value.assignedCN
        this.code = event.value.code
        this.assignedRole = event.value.role
        this.onValueChange()
      },
      onChangeDecision: function(event) {
        this.receiverTypes = null
        this.decision = event.value
        if(this.d.receiverTypes){
          this.receiverTypes = this.getReceiverTypeOptions(null,null)
          this.receiverType = null
          // this.receiverDirection = null
          this.updateDirection(null)
          // if(this.d.receiverTypes.includes("multiple")){
            switch(this.decision){
              case "approve":
                this.updateDirection("up")
                // this.receiverDirection = { direction: "up" }
                this.receiverTypes = this.getReceiverTypeOptions(["single"], null)
                break
              case "redirect":
                if(this.d.receiverTypes.includes("multiple")) this.receiverTypes = this.getReceiverTypeOptions(["single","multiple"], null)
                else this.receiverTypes = this.getReceiverTypeOptions(["single"], null)
                break
              case "requestModification":
                this.receiverTypes = this.getReceiverTypeOptions(["single"], null)
                break
              case "reject":
                this.receiverTypes = this.getReceiverTypeOptions(null,null)
                break
            }
          // }
          // else if(this.d.receiverTypes.includes("single")) {
          //   this.receiverTypes = this.getReceiverTypeOptions(null,null)
          //   if(this.decision == "approve") this.updateDirection("up")
          //   this.receiverTypes = this.getReceiverTypeOptions(["single"], "single")
          // }
        }
        this.onValueChange()
      },
      onChangeReceiverType: function(event) {
        this.receiverType = event.value
        this.onValueChange()
      },
      getDecisionOptions: function(decisions, value) {
        if(!decisions) return null
        let options = [];
        for(let i in decisions){
          options.push({
            name: this.getDecisionLabel(decisions[i]),
            value: decisions[i]
          })
        }
        if(options.length == 0) return null
        return {options: options, value: value}
      },
      getReceiverTypeOptions: function(receiverTypes, value) {
        if(!receiverTypes) return null
        let options = [];
        for(let i in receiverTypes){
          options.push({
            name: this.getReceiverTypeLabel(receiverTypes[i]),
            value: receiverTypes[i]
          })
        }
        if(value) this.receiverType = value
        return {options: options, value: value}
      },
      getDecisionLabel: function(value) {
        switch (value) {
          case "approve":
            return "request-approve"
          case "redirect":
            return "request-redirection"
          case "requestModification":
            return "request-modification"
          case "reject":
            return "request-rejection"
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
      updateDirection: function(direction) {
        this.receiverDirection = {
          direction: (this.d.decisions)? direction:this.direction,
          minimumLevel: this.minimumLevel
        }
      }
    },
    watch: {
      val: function (newVal) {
        if(newVal.direction){
          this.direction = newVal.direction
        }
        if(newVal.minimumLevel){
          this.minimumLevel = newVal.minimumLevel
        }
        if(newVal.fields){
          if(!(newVal.fields instanceof Array)) newVal.fields = [newVal.fields]
        }
        if(newVal.decisions){
          if(!(newVal.decisions instanceof Array)) newVal.decisions = [newVal.decisions]
          this.decisions = this.getDecisionOptions(newVal.decisions)
        }
        if(newVal.receiverTypes){
          if(!(newVal.receiverTypes instanceof Array)) newVal.receiverTypes = [newVal.receiverTypes]
          if(newVal.decisions){
            this.receiverTypes = null
          }else if(newVal.receiverTypes instanceof Array && newVal.receiverTypes.length == 1){
            this.receiverTypes = this.getReceiverTypeOptions(newVal.receiverTypes ,newVal.receiverTypes[0])
            this.receiverType = newVal.receiverTypes[0]
          }else{
            this.receiverTypes = this.getReceiverTypeOptions(newVal.receiverTypes)
          }
        }

        if(this.firstTime){
          if(newVal.fields || newVal.decisions || newVal.receiverTypes){
            this.d = newVal
          }
          this.updateDirection()
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
      if(this.val.decisions){
        this.receiverTypes = null
      }else if(this.val.receiverTypes instanceof Array && this.val.receiverTypes.length == 1){
        this.receiverTypes = this.getReceiverTypeOptions(this.val.receiverTypes ,this.val.receiverTypes[0])
        this.receiverType = this.val.receiverTypes[0]
      }else{
        this.receiverTypes = this.getReceiverTypeOptions(this.val.receiverTypes)
      }

      this.userDetails = await this.getUserDetails()
      this.displayName = this.userDetails.displayName
      this.parent = await this.getParentDetails()
      this.updateDirection()
      this.onValueChange()
    }
  }
</script>