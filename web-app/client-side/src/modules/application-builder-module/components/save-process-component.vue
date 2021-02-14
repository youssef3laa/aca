<template>
    <div>
    <v-card outlined>
        <RadioGroupComponent :field="{ name: 'decision', title: 'decision' }"
                             :val="decisions"
                             @update="onChangeDecision"></RadioGroupComponent>
        <v-card-text v-if="decision=='tempSave'">
            <v-row>
                <v-col cols="4">
                    <DatePickerComponent :field="datePicker.field"
                                         :val="datePicker.val"
                                         :model="datePicker.model"
                                         @update="onChangeDate"></DatePickerComponent>
                </v-col>
            </v-row>
        </v-card-text>
    </v-card>
    </div>
</template>

<script>
    import RadioGroupComponent from '../components/radio-group-component'
    import DatePickerComponent from '../components/datePicker-component'

    export default {
        name: "SaveProcessComponent",
        props: ["val","field"],
        components: {
            RadioGroupComponent,
            DatePickerComponent
        },
        data() {
            return {
                d: this.val,
                decisions : {
                    options: [{name: "save", value: "save"}, {name: "temporarySave", value: "tempSave"}],
                    value: "save"
                },
                datePicker: {
                    field: {
                        label: "displayDate",
                        name: "displayDate",
                        rule: "required",
                        min: "minDate",
                        col: "4"
                    },
                    val: {},
                    model: {
                        minDate: new Date().toISOString().split("T")[0]
                    }
                },
                decision: "save",
                displayDate: new Date().toISOString().split("T")[0]
            }
        },
        methods: {
            onValueChange: function() {
                this.$emit('update', {
                    name: this.field.name,
                    value: {
                        decision: this.decision,
                        displayDate: this.displayDate
                    }
                })
            },
            onChangeDecision: function(event){
                this.decision = event.value
                this.onValueChange()
            },
            onChangeDate: function(event){
                this.displayDate = event.value
                this.onValueChange()
            }
        },
        watch: {
            val: function (newVal) {
                this.d = newVal
            }
        },
        mounted() {
            this.onValueChange()
        }
    }
</script>