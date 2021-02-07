<template>
    <v-row>
        <v-col :cols="4" :md="4">
            <AutocompleteComponent
                    :field="agency.field"
                    :val="agency.val"
                    @update="onChangeAgency"></AutocompleteComponent>
        </v-col>
        <v-col :cols="4" :md="4" v-if="minimumLevel ==  null || minimumLevel ==  'sector' || minimumLevel == 'office' ||  minimumLevel == 'group' || minimumLevel == 'member'">
            <AutocompleteComponent
                    :field="sector.field"
                    :val="sector.val"
                    @update="onChangeSector"></AutocompleteComponent>
        </v-col>
        <v-col :cols="4" :md="4" v-if="minimumLevel ==  null || minimumLevel == 'office' ||  minimumLevel == 'group' || minimumLevel == 'member'">
            <AutocompleteComponent
                    :field="office.field"
                    :val="office.val"
                    @update="onChangeAdminOffice"></AutocompleteComponent>
        </v-col>
        <v-col :cols="4" :md="4" v-if="minimumLevel ==  null || minimumLevel == 'group' || minimumLevel == 'member'">
            <AutocompleteComponent
                    :field="group.field"
                    :val="group.val"
                    @update="onChangeGroup"></AutocompleteComponent>
        </v-col>
        <v-col :cols="4" :md="4" v-if="minimumLevel ==  null || minimumLevel == 'member'">
            <AutocompleteComponent
                    :field="member.field"
                    :val="member.val"
                    @update="onChangeMember"></AutocompleteComponent>
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
                direction: (this.val)?this.val.direction:null,
                minimumLevel: (this.val)?this.val.minimumLevel:null,
                userDetails: null,
                parent: null,
                higher: false,
                agencyLabel: 'theAgency',
                sectorLabel: 'theSector',
                officeLabel: 'theAdminOffice',
                groupLabel: 'theGroup',
                memberLabel: 'theMember',
                code: null,
                assignedCN: null,
                assignedRole: null,
                agencySelected: null,
                sectorSelected: null,
                officeSelected: null,
                groupSelected: null,
                memberSelected: null,
                agency: {
                    field: this.getAutocompleteField('theAgency',false),
                    val: (this.field.readonly)? this.fillAutocomplete(this.val.agency) : this.getAutocompleteVal('')
                },
                sector: {
                    field: this.getAutocompleteField('theSector',false),
                    val: (this.field.readonly)? this.fillAutocomplete(this.val.sector) : this.getAutocompleteVal('')
                },
                office: {
                    field: this.getAutocompleteField('theAdminOffice',false),
                    val: (this.field.readonly)? this.fillAutocomplete(this.val.office) : this.getAutocompleteVal('')
                },
                group: {
                    field: this.getAutocompleteField('theGroup',false),
                    val: (this.field.readonly)? this.fillAutocomplete(this.val.group) : this.getAutocompleteVal('')
                },
                member: {
                    field: this.getAutocompleteField('theMember',false),
                    val: (this.field.readonly)? this.fillAutocomplete(this.val.member) : this.getAutocompleteVal('')
                }
            }
        },
        methods: {
            onValueChange: async function() {
                if(this.field.readonly) return
                await this.handleReceiverFormOutput()
                this.$emit('update', {
                    name: this.field.name,
                    higher: this.higher,
                    direction: this.direction ,
                    parent: this.parent ,
                    value: {
                        agency: this.agencySelected ,
                        sector: this.sectorSelected ,
                        office: this.officeSelected ,
                        group: this.groupSelected ,
                        member: this.memberSelected,
                        assignedCN: this.assignedCN,
                        code: this.code,
                        role: this.assignedRole
                    }
                })
            },
            onChangeAgency: function(event) {
                if(this.field.readonly || this.agency.field.readonly) return
                this.agencySelected = null
                this.sector.val = this.getAutocompleteVal('')
                if (event.value.value) {
                    this.agencySelected = event.value.value.object
                    if(this.direction && this.direction.includes("up")){
                        this.sector.val = this.getAutocompleteVal('org/unit/'+this.userDetails.groups[0].unit.unitCode+'/up/all/unitTypeCode/SCT')
                    }
                    else if(this.checkCanSelectNext(event.value.value.value,"AGN")) {
                        this.sector.val = this.getAutocompleteVal('org/unit/' + event.value.value.value + '/down/all/unitTypeCode/SCT')
                    }
                }
                this.office.val = this.getAutocompleteVal('')
                this.group.val = this.getAutocompleteVal('')
                this.member.val = this.getAutocompleteVal('')
                this.onValueChange()
            },
            onChangeSector: function(event) {
                if(this.field.readonly || this.sector.field.readonly) return
                this.sectorSelected = null
                this.office.val = this.getAutocompleteVal('')
                if (event.value.value) {
                    this.sectorSelected = event.value.value.object
                    if(this.direction && this.direction.includes("up")){
                        this.office.val = this.getAutocompleteVal('org/unit/'+this.userDetails.groups[0].unit.unitCode+'/up/all/unitTypeCode/ADM,OFC')
                    }
                    else if(this.checkCanSelectNext(event.value.value.value,"SCT")) {
                        this.office.val = this.getAutocompleteVal('org/unit/' + event.value.value.value + '/down/all/unitTypeCode/ADM,OFC')
                    }
                }
                this.group.val = this.getAutocompleteVal('')
                this.member.val = this.getAutocompleteVal('')
                this.onValueChange()
            },
            onChangeAdminOffice: function(event) {
                if(this.field.readonly || this.office.field.readonly) return
                this.officeSelected = null
                this.group.val = this.getAutocompleteVal('')
                if(event.value.value){
                    this.officeSelected = event.value.value.object
                    if(this.direction && this.direction.includes("up")){
                        this.group.val = this.getAutocompleteVal('org/unit/'+this.userDetails.groups[0].unit.unitCode+'/up/all/unitTypeCode/GRP')
                    }
                    else if(this.checkCanSelectNext(event.value.value.value,"OFC,ADM")) {
                        this.group.val = this.getAutocompleteVal('org/unit/' + event.value.value.value + '/down/all/unitTypeCode/GRP')
                    }
                }
                this.member.val = this.getAutocompleteVal('')
                this.onValueChange()
            },
            onChangeGroup: function(event) {
                if(this.field.readonly || this.group.field.readonly) return
                this.groupSelected = null
                this.member.val = this.getAutocompleteVal('')
                if (event.value.value) {
                    this.groupSelected = event.value.value.object
                    if(this.direction && this.direction.includes("up")){
                        this.member.val = this.getAutocompleteVal('org/unit/'+this.userDetails.groups[0].unit.unitCode+'/up/all/unitTypeCode/GRP')
                    }
                    else if(this.checkCanSelectNext(event.value.value.value,"GRP")) {
                        this.member.val = this.getAutocompleteVal('org/group/findByUnitCodes/' + event.value.value.value)
                    }
                }
                this.onValueChange()
            },
            onChangeMember: function(event) {
                if(this.field.readonly || this.member.field.readonly) return
                if(event.value.value){
                    this.memberSelected = event.value.value.object
                }else{
                    this.memberSelected = null
                }
                this.onValueChange()
            },
            handleReadOnlyReceiver: function(unitCode,unitTypeCode,headRole) {
                if(this.direction == "all") {
                    this.handleDirectionAll()
                }else if(this.direction == "up"){
                    this.handleDirectionUp(unitCode,unitTypeCode,headRole, false)
                }else if(this.direction =="up-with"){
                    this.handleDirectionUp(unitCode,unitTypeCode,headRole, true)
                }else if(this.direction =="down"){
                    this.handleDirectionDown(unitCode,unitTypeCode,headRole, false)
                }else{
                    this.handleDirectionDown(unitCode,unitTypeCode,headRole, true)
                }
            },
            handleDirectionAll: function (){
                this.agency.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/AGN')
            },
            handleDirectionDown: function (unitCode,unitTypeCode,headRole,sameLevel) {
                this.disablePrevious(unitTypeCode, headRole)
                switch (unitTypeCode) {
                    case "AGN":
                        if(sameLevel)
                            this.agency.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/AGN')
                        else {
                            this.disablePrevious("SCT",headRole)
                            this.sector.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/SCT')
                        }
                        return
                    case "SCT":
                        if(sameLevel)
                            this.sector.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/SCT')
                        else {
                            this.disablePrevious("OFC",headRole)
                            this.office.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/ADM,OFC')
                        }
                        return
                    case "OFC":
                    case "ADM":
                        if(sameLevel)
                            this.office.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/ADM,OFC')
                        else {
                            this.disablePrevious("OFC",headRole)
                            this.group.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/GRP')
                        }
                        return
                    case "GRP":
                        if(headRole){
                            if(sameLevel)
                                this.group.val = this.getAutocompleteVal('org/unit/COC/down/all/unitTypeCode/GRP')
                            else {
                                this.disablePrevious("GRP",headRole)
                                this.member.val = this.getAutocompleteVal('org/group/findByUnitCodes/'+unitCode)
                            }
                        }else{
                            this.member.val = this.getAutocompleteVal('org/group/findByUnitCodes/'+unitCode)
                        }
                        return
                    default:
                        this.agency.val = this.getAutocompleteVal('org/unit/'+unitCode+'/down/all/unitTypeCode/AGN')
                        this.higher = true
                        return
                }
            },
            handleDirectionUp: function (unitCode,unitTypeCode,headRole,sameLevel) {
                this.disableNext(unitTypeCode,headRole)
                switch (unitTypeCode) {
                    //TODO: ASK Mina For ('org/unit/'+unitCode+'/up/all/unitTypeCode/AGN')
                    case "SCT":
                        if(!sameLevel) this.disableNext("AGN",headRole)
                        this.agency.val = this.getAutocompleteVal('org/unit/'+unitCode+'/up/all/unitTypeCode/AGN')
                        return
                    case "OFC":
                        if(!sameLevel) this.disableNext("SCT",headRole)
                        this.agency.val = this.getAutocompleteVal('org/unit/'+unitCode+'/up/all/unitTypeCode/AGN')
                        return
                    case "ADM":
                        if(!sameLevel) this.disableNext("SCT",headRole)
                        this.agency.val = this.getAutocompleteVal('org/unit/'+unitCode+'/up/all/unitTypeCode/AGN')
                        return
                    case "GRP":
                        if(!sameLevel) this.disableNext("ADM",headRole)
                        this.agency.val = this.getAutocompleteVal('org/unit/'+unitCode+'/up/all/unitTypeCode/AGN')
                        return
                    default:
                        this.disableNext()
                        this.higher = true
                        return
                }
            },
            disablePrevious: function(level,headRole){
                if(level == "SCT" || level == "OFC" || level == "ADM" || level == "GRP" || level == null){
                    this.agency.field = this.getAutocompleteField(this.agencyLabel, true)
                }
                if(level == "OFC" || level == "ADM" || level == "GRP" || level == null){
                    this.sector.field = this.getAutocompleteField(this.sectorLabel, true)
                }
                if(level == "GRP" || level == null){
                    this.office.field = this.getAutocompleteField(this.officeLabel, true)
                }
                if((level == "GRP" && !headRole) || level == null){
                    this.group.field = this.getAutocompleteField(this.groupLabel, true)
                }
                if(level == null){
                    this.member.field = this.getAutocompleteField(this.memberLabel, true)
                }
            },
            disableNext: function(level,headRole){
              if(level == null){
                  this.agency.field = this.getAutocompleteField(this.agencyLabel, true)
              }
              if(level == "AGN" || level == null){
                  this.sector.field = this.getAutocompleteField(this.sectorLabel, true)
              }
              if(level == "AGN" || level == "SCT" || level == null){
                  this.office.field = this.getAutocompleteField(this.officeLabel, true)
              }
              if(level == "AGN" || level == "SCT" || level == "OFC" || level == "ADM" || level == null){
                  this.group.field = this.getAutocompleteField(this.groupLabel, true)
              }
              if(level == "AGN" || level == "SCT" || level == "OFC" || level == "ADM" || (level == "GRP" && headRole) || level == null){
                  this.member.field = this.getAutocompleteField(this.memberLabel, true)
              }
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
            fillComponent() {
                this.agency.val = this.fillAutocomplete(this.val.agency)
                this.sector.val = this.fillAutocomplete(this.val.sector)
                this.office.val = this.fillAutocomplete(this.val.office)
                this.group.val = this.fillAutocomplete(this.val.group)
                this.member.val = this.fillAutocomplete(this.val.member)
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
            checkCanSelectNext: function(unitCode,unitTypeCode) {
                console.log(unitCode, unitTypeCode)
                if(this.direction != "up"){
                    if(unitTypeCode.includes(this.userDetails.groups[0].unit.unitTypeCode)){
                        return (this.userDetails.groups[0].unit.unitCode == unitCode)
                    }
                    return true
                }
                return true
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
                    this.onValueChange()
                }
            }
        },
        async mounted() {
            if(!this.field.readonly){
                this.userDetails = await this.getUserDetails()
                this.handleReadOnlyReceiver(this.userDetails.groups[0].unit.unitCode,
                                            this.userDetails.groups[0].unit.unitTypeCode,
                                            this.userDetails.groups[0].isHeadRole)
                this.parent = await this.getParentDetails()
                console.log("User", this.userDetails)
                console.log("Parent", this.parent)
                await this.onValueChange()
            }
        }
    }
</script>