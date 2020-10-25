<template>
      <v-form
      ref="form"
      v-model="valid"
      lazy-validation
    >
      <v-text-field
        v-model="username"
        :counter="10"
        :rules="usernameRules"
        label="Username"
        required
      ></v-text-field>
  
      <v-text-field
        v-model="password"
        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[rules.required, rules.min]"
        :type="show1 ? 'text' : 'password'"
        name="input-10-1"
        hint="At least 8 characters"
        counter
        @click:append="show1 = !show1"
      >
      <template #label>
        <span v-t="'password'"></span>
      </template>
      </v-text-field>
  
      <v-checkbox
        v-model="checkbox"
        :rules="[v => !!v || 'You must agree to continue!']"
        label="Do you agree?"
        required
      ></v-checkbox>


       <v-btn
        :disabled="!valid"
        color="success"
        class="mr-3"
        @click="submit"
      >
        Login
      </v-btn>

      <v-btn
        :disabled="!valid"
        color="success"
        class="mr-3"
        @click="validate"
      >
        Validate
      </v-btn>
  
      <v-btn
        color="error"
        class="mr-3"
        @click="reset"
      >
        Reset Form
      </v-btn>
  
      <v-btn
        color="warning"
        @click="resetValidation"
      >
        Reset Validation
      </v-btn>
    </v-form>
    
    
</template>

<script>
// console.log(this.$vuetify.lang.current)
export default {

  name: 'LoginView',
  data: () => ({
    valid: true,
    username: '',
    show1: false,
    usernameRules: [
      v => !!v || 'Name is required',
      v => (v && v.length <= 10) || 'Name must be less than 10 characters',
    ],
    password: '',
    rules: {
        required: value => !!value || 'Required.',
        min: v => v.length >= 8 || 'Min 8 characters',
        emailMatch: () => (`The email and password you entered don't match`),
    },
    select: null,
    checkbox: false,
  }),

  methods: {
    validate () {
      this.$refs.form.validate()
    },
    reset () {
      this.$refs.form.reset()
    },
    resetValidation () {
      this.$refs.form.resetValidation()
    },
    submit(){

    }
  },
  i18n: {
    messages: '../../../locales/en.json'
  }

}
</script>