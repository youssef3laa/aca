<template>
  <div>
    <v-container grid-list-md>
      <form action="">
        <v-row>
          <v-col>
            <label for="Fname"> FirstName</label>
            <input type="text" name="Fname" style="background-color:white" v-model="Fname" />
          </v-col>
          <v-col>
            <label for="Lname">Last Name </label>
            <input type="text" name="Lname" id="" style="background-color:white" v-model="Lname" />
          </v-col>
          <v-col>
            <label for="email">email </label>
            <input type="text" name="email" id="" style="background-color:white" v-model="Email" />
          </v-col>
        </v-row>
        <v-row>
          <button v-on:click="create">Create Employee</button>
        </v-row>
      </form>
    </v-container>
  </div>
</template>

<script>
// import http from '../modules/core-module/services/http';
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
      router.go(0);
      // http.post('http://localhost:9000/api/employee/initiate/',employee)
      //   .then((response) => {
      //     console.log(response);
      //   }).catch((error) => {
      //     console.error(error);
      //   })
    }
  }
}
</script>
