<template>
  <div>
    <v-container grid-list-md>
      <form action="">
        <v-row>
          <v-col>
            <label for="Fname"> FirstName</label>
            <input type="text" name="Fname" style="background-color:white" />
          </v-col>
          <v-col>
            <label for="Lname">Last Name </label>
            <input type="text" name="Lname" id="" style="background-color:white" />
          </v-col>
          <v-col>
            <label for="email">email </label>
            <input type="text" name="email" id="" style="background-color:white" />
          </v-col>
        </v-row>
      </form>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'EmployeeData',
}
</script>
