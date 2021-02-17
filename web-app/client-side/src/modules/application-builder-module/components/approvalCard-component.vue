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
            <TextareaComponent :field="{ name: 'routingNotes', label: 'notes' }"
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
            <TextareaComponent :field="{ name: 'routingOpinion', label: 'opinion' }"
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
          this.updateDirection(null)
            switch(this.decision){
              case "president":
                this.assignee = "cn=HTCA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com"
                this.code = "ADM"
                this.assignedRole = null
                break
              case "vice":
                this.assignee = "cn=HTVA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com"
                this.code = "ADM"
                this.assignedRole = null
                break
              case "approve":
                this.updateDirection("up")
                this.receiverTypes = this.getReceiverTypeOptions(["single"], null)
                break
              case "redirect":
                this.updateDirection("down-with")
                if(this.d.receiverTypes.includes("multiple")) this.receiverTypes = this.getReceiverTypeOptions(["single","multiple"], null)
                else this.receiverTypes = this.getReceiverTypeOptions(["single"], null)
                break
              case "requestModification":
                this.updateDirection("down")
                this.receiverTypes = this.getReceiverTypeOptions(["single"], null)
                break
              case "reject":
                this.receiverTypes = this.getReceiverTypeOptions(null,null)
                break
            }
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
            name: decisions[i],
            label: this.getDecisionLabel(decisions[i])
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
            name: receiverTypes[i],
            label: this.getReceiverTypeLabel(receiverTypes[i]),
          })
        }
        if(value) this.receiverType = value
        return {options: options, value: value}
      },
      getDecisionLabel: function(value) {
        switch (value) {
          case "president":
            return "request-president-approval"
          case "vice":
            return "request-vice-approval"
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
      },
      handleNewVal: function(newVal){
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
      }
    },
    watch: {
      val: function (newVal) {
        this.handleNewVal(newVal)

        if(this.d){
          for(let key in newVal){
            this.d[key] = newVal[key]
          }
        }else{
          this.d = newVal
        }

        if(this.firstTime){
          this.firstTime=false
          this.onValueChange()
        }
      }
    },
    async created() {
      this.handleNewVal(this.val)

      this.userDetails = await this.getUserDetails()
      this.displayName = this.userDetails.displayName
      this.parent = await this.getParentDetails()
      this.updateDirection()
      this.onValueChange()
    }
  }
</script>