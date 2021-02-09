<template>
  <v-menu v-model="menu" :close-on-content-click="false" max-width="290">
    <template v-slot:activator="{ on, val }">
      <validation-provider
        :name="field.name"
        :rules="field.rule"
        v-slot="{ errors }"
        :vid="field.name"
      >
        <v-text-field
          v-bind="val"
          v-on="on"
          :value="computedDateFormattedLuxonjs"
          clearable
          color="outline"
          outlined
          @click:clear="
            date = null
            datePickerChanged()
          "
        >
          <template #label>
            <span v-t="field.label"></span>
          </template>
        </v-text-field>
        <span class="red--text">{{ errors[0] }}</span>
      </validation-provider>
    </template>
    <v-date-picker
      v-model="date"
      @change="
        datePickerChanged()
        menu = false
      "
    ></v-date-picker>
  </v-menu>
</template>
<script>
import { DateTime } from 'luxon'

export default {
  data() {
    return {
      date: new Date().toISOString().split('T')[0],
      menu: false,
    }
  },

  props: ['val', 'field'],
  methods: {
    datePickerChanged: function() {
      console.log('datePickerChanged')
      this.$emit('update', {
        name: this.field.name,
        value: this.date,
        type: 'datePickerChange',
      })
    },
  },
  mounted() {
    this.datePickerChanged()
  },
  computed: {
    computedDateFormattedLuxonjs() {
      return DateTime.fromISO(this.date).toFormat('yyyy/MM/dd')
    },
  },
  watch: {
    val: function(newVal) {
      this.date = newVal
    },
  },
}
</script>
