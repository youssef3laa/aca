<template>
  <div>
    <v-container grid-list-md>
      <form action="">
        <v-row>
          <v-col>
            <v-text-field outlined filled label="First Name" name="Fname" v-model="Fname" />
          </v-col>
          <v-col>
            <v-text-field outlined filled label="Last Name" name="Lname" v-model="Lname" />
          </v-col>
          <v-col>
            <v-text-field outlined filled label="Email" name="email" v-model="Email" />
          </v-col>
          <v-col>
            <v-btn v-on:click="create">Create Employee</v-btn>
          </v-col>
        </v-row>
      </form>
    </v-container>
  </div>
</template>

<script>
import http from '../modules/core-module/services/http';
import router from '../router'
export default {
  name: 'EmployeeData',
  data() {
    return {
      Fname: null,
      Lname: null,
      Email: null
    }
  },
  methods: {
    create: function() {
      event.preventDefault()
      var employee = {
        'Fname': this.Fname ,
        'Lname': this.Lname ,
        'Email': this.Email
      }
      console.log(employee);
      http.post('employee/initiate/',employee)
        .then((response) => {
            console.log(response);
            router.go(0);
        }).catch((error) => {
          console.error(error);
        })
    }
  }
}
</script>
