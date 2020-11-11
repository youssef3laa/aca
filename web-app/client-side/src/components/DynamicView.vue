<template>
  <div>
    <!-- <formBuilder /> -->
    <component v-bind:is="currentTabComponent"></component>
  </div>
</template>

<script>
// import formBuilder from './FormGenerator'
import axios from 'axios'
export default {
  // Vue.component("AsyncCmp", () => import("./AsyncCmp"));

  components: {
    formBuilder: () => import('./' + this.post),
  },

  //   components: { formBuilder  => Vue.component('formBuilder', ()=>'./FormGenerator')},
  data() {
    return {
      currentTabComponent: 'formBuilder',
    }
  },
  created() {
    axios
      .get('http://localhost:9000/api/vue/components/get/', {
        headers: {
          'X-Auth-Token':
            'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwib21hcnNhYnJ5XCIsXCJwYXNzd29yZFwiOlwiQXNzZXQ5OWFcIixcIm9yZ2FuaXphdGlvblwiOlwibW9kXCIsXCJTQU1MYXJ0XCI6XCIqT1REU1NTTypBWkJCUWxKb2RGWmpZbG90WDBONGJqZEZORkJoUkdOUmNsUkdRVTB0VTJkQlVXdEZNSHBTWTFaTU1rTXRUV2hvY3pSUFZHRkhSMmRGUVRBdFZXbGpRbFJWZFRSU1VUaFlUbWd6U3pZM1h6VmFRbHBpVmtWT2VISnhhVWQwVVhCVVVqZGlURGhwZVdWa2VrcDViVlpaWDNsa2FtNXlNRm8xTUhKRWIyWkhNMEk1YlVzMlUwcENVbTVHYTJGeVVXcGZNamxmWVdSMWMxTk5UMFpZWVVwTU1uTlVTbGt5VjNWRVdFbzJaa2xOYlRWRlowSkhkVE5RVEd4NmEyTkNjRXMwVTNseGFFTlJkRVpzVVZCSVozTlZRalZIV25wZlNGVTRWVXhsWTA5RFJFaFlTWFZ3TjBSWlMyVnFaRTl1ZUVScE1UbFRXWEZWU3paTlFuTkJYM3B3VldORlptazBSVEUxTjJaV1FUbHBUWEF5U3pKaE5VVlFWRFJMU0ZoemRYZFBkRlowZUVORVJuaGZkRFpWVXpkS1ZFRnhRWEZJVTNSUmQxbGZZbEp2YkdGdlRuaG1lbWhEUlZwNllqQlVNbFJHYW5GNFdsaFBOMVJ4UzBkaWRTMHRlSFl0YjFaS2JrczFZV3RVU0dOSldXSklaV1JGTjJKWE5qbHRUbWRuTTIxTFlXWnpRbTFxVUhWR1dHNVNiVXRHUVNvcUFFNEFTZ0FVLVNIUGhLblM0Yk1KNHVrV0hraUtfTEFQOTJzQUVCb2RoMzVpdERFaU95STNIcEJydUtZQUlFc1g3X1NGSU16MXdLNlVaVnp6ZzZMaURyN0I0R2FnM01NZWtzRjZieEdfQUFBKlwifSJ9.HtLC8luIUjxe5aYzRJFXetoRSbIssTMqTBUmpttkMLNNY0qIxZ1v6ysOzPbPjPpF6yEeYmuTDa9uF5JJRaNwFQ',
        },
      })
      .then((response) => {
        console.log(response)
        this.components.formBuilder = () => import('./' + response)
      })
      .catch((e) => {
        console.log(e)
      })
  },
}
</script>
