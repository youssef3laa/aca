<template>
  <!-- <v-menu
    ref="menu"
    v-model="menu"
    :close-on-content-click="false"
    :return-value.sync="date"
    transition="scale-transition"
    offset-y
    min-width="290px"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-model="date"
        :label="field.label"
        prepend-icon="mdi-calendar"
        readonly
        v-bind="attrs"
        v-on="on"
        outlined
        color="outline"
      ></v-text-field>
    </template>
    <v-date-picker v-model="date" no-title scrollable>
      <v-spacer></v-spacer>
      <v-btn text color="primary" @click="menu = false">
        Cancel
      </v-btn>
      <v-btn text color="primary" @click="$refs.menu.save(date)">
        OK
      </v-btn>
    </v-date-picker>
  </v-menu> -->
  <v-menu v-model="menu" :close-on-content-click="false" max-width="290">
    <template v-slot:activator="{ on, val }">
      <v-text-field
          v-bind="val"
          v-on="on"
          :label="field.label"
          :value="computedDateFormattedLuxonjs"
          clearable
          color="outline"
          outlined
          @click:clear="date = null"
      ></v-text-field>
    </template>
    <v-date-picker v-model="date" @change="datePickerChanged(); menu = false;"></v-date-picker>
  </v-menu>
</template>
<script>
import {DateTime} from "luxon";

export default {
  data() {
    return {
      date: new Date().toISOString().split('T')[0],
      menu: false,
    }
  },

  props: ['val', 'field'],
  methods: {
    datePickerChanged: function () {
      console.log("datePickerChanged");
      this.$emit("update", {
        name: this.field.name,
        value: this.date,
        type: 'datePickerChange'
      })
    }
  },
  mounted() {
    this.datePickerChanged();
  },
  computed: {
    computedDateFormattedLuxonjs() {

      return DateTime.fromISO(this.date).toFormat('yyyy/MM/dd');
    },
  },
}
</script>
