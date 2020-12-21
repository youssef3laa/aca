import http from "../modules/core-module/services/http";

export default {
    methods: {
        loadForm: async function (formName) {
            http
                .get("/user/form/"+formName)
                .then((response) => {
                    this.$refs.appBuilder.setAppData(response.data.data.app);
                })
                .catch((error) => console.error(error));
        },
        initiateProcess: async function (data){
            http
          .post("/process/initiate", data)
          .then((response) => {
            console.log(response);
          })
          .catch((error) => console.error(error));
        }
    }
}