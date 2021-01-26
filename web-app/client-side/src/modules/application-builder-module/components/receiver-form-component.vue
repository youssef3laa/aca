<template>
    <v-row>
        <v-col :cols="4" :md="4">
            <AutocompleteComponent
                    :field="agency.field"
                    :val="agency.val"
                    @update="onChangeAgency"></AutocompleteComponent>
        </v-col>
        <v-col :cols="4" :md="4">
            <AutocompleteComponent
                    :field="sector.field"
                    :val="sector.val"
                    @update="onChangeSector"></AutocompleteComponent>
        </v-col>
        <v-col :cols="4" :md="4">
            <AutocompleteComponent
                    :field="adminOffice.field"
                    :val="adminOffice.val"
                    @update="onChangeAdminOffice"></AutocompleteComponent>
        </v-col>
    </v-row>
</template>

<script>
    import AutocompleteComponent from './autocomplete-component'
    import userMixin from "../../../mixins/userMixin"

    export default {
        components: {AutocompleteComponent},
        mixins: [userMixin],
        props: ["val","field"],
        data() {
            return {
                d: this.val,
                userDetails: null,
                agencyLabel: 'theAgency',
                sectorLabel: 'theSector',
                adminOfficeLabel: 'theAdminOffice',
                agency: {
                    field: {
                        name: 'theAgency',
                        readonly: false
                    },
                    val: {
                        url: 'org/group/findByCodes/HGCS,HRCA,HCAO'
                    }
                },
                sector: {
                    field: {
                        name: 'theSector',
                        readonly: false
                    },
                    val: {
                        url: ''
                    }
                },
                adminOffice: {
                    field: {
                        name: 'theAdminOffice',
                        readonly: false
                    },
                    val: {
                        url: ''
                    }
                }
            }
        },
        watch: {
            val: function (newVal) {
                for(var key in newVal){
                    this.d[key] = newVal[key]
                }
                console.log("Val Watch", this.d)
            }
        },
        methods: {
            onValueChange: function() {
                this.$emit('update', {
                    name: this.field.name,
                    value: {
                        assignee: "alyy"
                    },
                })
            },
            onChangeAgency: function(event) {
                console.log("Agency", event)
                if(event.value.value){
                    this.sector.val = {
                        url : 'org/group/findByCodes/HGCS,HRCA,HCAO'
                    }
                    this.adminOffice.val = {
                        url : ''
                    }
                }else{
                    this.sector.val = {
                        url : ''
                    }
                    this.adminOffice.val = {
                        url : ''
                    }
                }
                // this.onValueChange()
            },
            onChangeSector: function(event) {
                console.log("Sector", event)
                if(event.value.value){
                    this.adminOffice.val = {
                        url : 'org/group/findByCodes/HGCS,HRCA,HCAO'
                    }
                }else{
                    this.adminOffice.val = {
                        url : ''
                    }
                }
                // this.onValueChange()
            },
            onChangeAdminOffice: function(event) {
                console.log("AdminOffice", event)
                // this.onValueChange()
            },
            handleReadOnlyReceiver: function(unitTypeCode) {
                console.log("UnitTypeCode", unitTypeCode)
                switch (unitTypeCode) {
                    case "SCT":
                        this.agency.field = {
                            readonly : true
                        }
                        this.sector.val = {
                            url : 'org/group/findByCodes/HGCS,HRCA,HCAO'
                        }
                        return
                    case "OFC":
                        this.agency.field = {
                            name: this.agencyLabel,
                            readonly : true
                        }
                        this.sector.field = {
                            name: this.sectorLabel,
                            readonly : true
                        }
                        this.agency.val = {
                            url : ''
                        }
                        this.adminOffice.val = {
                            url : 'org/group/findByCodes/HGCS,HRCA,HCAO'
                        }
                        return
                    case "ADM":
                        this.agency.field = {
                            name: this.agencyLabel,
                            readonly : true
                        }
                        this.sector.field = {
                            name: this.sectorLabel,
                            readonly : true
                        }
                        this.adminOffice.val = {
                            url : 'org/group/findByCodes/HGCS,HRCA,HCAO'
                        }
                        return
                }
            },
        },
        async mounted() {
            this.userDetails = await this.getUserDetails()
            this.handleReadOnlyReceiver(this.userDetails.groups[0].unit.unitTypeCode)
        }
    }
</script>

<style scoped>

</style>