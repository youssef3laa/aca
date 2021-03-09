<template>
  <v-menu
    ref="menu"
    v-model="menu"
    :close-on-content-click="false"
    max-width="290"
  >
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
          readonly
          @click:clear="
            date = null;
            datePickerChanged();
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
        datePickerChanged();
        menu = false;
      "
      reactive
      :no-title="field.format == 'year'"
      @click:year="saveYear"
      :max="max"
      :min="min"
      ref="picker"
    ></v-date-picker>
  </v-menu>
</template>
<script>
import { DateTime } from "luxon";

export default {
  data() {
    return {
      date: new Date().toISOString().split("T")[0],
      menu: false,
      max: null,
      min: null,
    };
  },

  props: ["val", "field", "model"],
  methods: {
    saveYear: function(date) {
      if (this.field.format != "year") return;

      this.date = date.toString();
      this.$refs.menu.save(date);
      this.$refs.picker.activePicker = "YEAR";
      this.menu = false;
      this.datePickerChanged();
    },
    datePickerChanged: function() {
      console.log("datePickerChanged");
      if (this.field.format == "year")
        this.date = DateTime.fromISO(this.date).toFormat("yyyy");
      this.$emit("update", {
        name: this.field.name,
        value: this.date,
        type: "datePickerChange",
      });
    },
  },
  mounted() {
    this.datePickerChanged();
  },
  computed: {
    computedDateFormattedLuxonjs() {
      if (this.field.format == "year") {
        return DateTime.fromISO(this.date).toFormat("yyyy");
      }
      return DateTime.fromISO(this.date).toFormat("yyyy/MM/dd");
    },

  },
  watch: {
    menu: function(val) {
      if (this.field.format == "year")
        val && this.$nextTick(() => (this.$refs.picker.activePicker = "YEAR"));
    },
    val: function(newVal) {
      if (newVal == "" || newVal == undefined || newVal == null)
        this.date = new Date().toISOString().split("T")[0];
      else this.date = newVal;
      if (this.field.max) {
        this.max = this.model[this.field.max];
        if (this.max < this.date) {
          this.date = new Date(this.max).toISOString().split("T")[0];
        }
      }
      if (this.field.min) {
        this.min = this.model[this.field.min];
        if (this.min > this.date) {
          this.date = new Date(this.min).toISOString().split("T")[0];
        }
      }
      if (this.field.readonly) {
        this.readonly = this.model[this.field.readonly];
      }
    },
  },
  created() {
    if (this.field.max) {
      this.max = this.model[this.field.max];
      if (this.max < this.date) {
        this.date = new Date(this.max).toISOString().split("T")[0];
      }
    }
    if (this.field.min) {
      this.min = this.model[this.field.min];
      if (this.min > this.date) {
        this.date = new Date(this.min).toISOString().split("T")[0];
      }
    }
    if (this.field.readonly) {
      this.readonly = this.model[this.field.readonly];
    }
  },
};
</script>
