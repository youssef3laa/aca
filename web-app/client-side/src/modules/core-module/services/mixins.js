import http from "@/modules/core-module/services/http";

export default {

    //TODO convert methods to async/await
    methods: {
        getPageConfig: function () {
            http
                .get("/user/form/" + this.taskUrl)
                .then((response) => {
                    console.log(this.getPageConfig.name);
                    this.$refs.appBuilders.setAppData(response.data.data.app);
                    this.claimTask();
                })
                .catch((error) => console.error(error));
        },
        claimTask: function () {
            http
                .post('/workflow/task/claim', this.taskId)
                .then((response) => {
                    console.log(this.claimTask.name);
                    console.log(response)
                    this.getTaskData()
                })
                .catch((error) => {
                    console.error(error)
                })
        },

    }
}
