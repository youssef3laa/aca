<template>
    <v-container class="fill-height">
        <v-row>
            <v-col :cols="4">
                <Charts
                        v-if="loaded"
                        :field="{ name: 'richTextChart', chartType: 'BarChart' }"
                        :val="{
            data: barChartData.data,
            labels: barChartData.labels,
            backgroundColor: richtextChart.backgroundColor,
          }"
                >
                </Charts>
            </v-col>
            <v-col :cols="4">
                <Charts
                        v-if="loaded"
                        :field="{ name: 'process', chartType: 'PieChart' }"
                        :val="{
            data: pieChartData.data,
            labels: pieChartData.labels,
            backgroundColor: processHistory.backgroundColor,
          }"
                >
                </Charts>
            </v-col>
            <v-col :cols="4">
                <Charts
                        v-if="loaded"
                        :field="{ name: 'process', chartType: 'DoughnutChart' }"
                        :val="{
            data: pieChartData.data,
            labels: pieChartData.labels,
            backgroundColor: processHistory.backgroundColor,
          }"
                >
                </Charts>
            </v-col>
        </v-row>
        <Topbar :val="{ tasks: taskList }"></Topbar>
         <OfficeGroudHeadInbox></OfficeGroudHeadInbox>

    </v-container>
</template>

<script>
    import chartsMixin from "../../../mixins/chartsMixin";
    import Charts from "../../application-builder-module/components/charts-component";
    import OfficeGroudHeadInbox from "../../application-builder-module/components/office-group-head-inbox"
    import userMixin from '../../../mixins/userMixin'
    import Topbar from "../../application-builder-module/components/topbar-component";
    export default {
        name: "HomePageOffice",
        components: {
            OfficeGroudHeadInbox,
            Charts, Topbar
        },
        mixins: [chartsMixin, userMixin],
        async mounted() {
            try {
                this.barChartData = await this.getDynamicReport(
                    this.richtextChart.requestData
                );
                this.pieChartData = await this.getDynamicReport(
                    this.processHistory.requestData
                );
                this.loaded = true;
                this.userDetails = await this.getUserDetails();
                console.log(this.userDetails)

            } catch (e) {
                console.error(e);
            }
        },
        methods: {
        },

        data() {
            return {
                taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
                response: [],
                chartsLoaded: 0,
                barChartData: {},
                pieChartData: {},
                loaded: false,
                userDetails:{},
                // barChartData:{},

                richtextChart: {
                    backgroundColor: ["#22B07D", "#22B07D", "#22B07D"],
                    requestData: {
                        table: "memoValues",
                        aggregations: [
                            {
                                function: "count",
                                column: {
                                    table: "memoValues",
                                    name: "jsonKey"
                                },
                            },
                        ],
                        columns: [{
                            table: "memoValues",
                            name: "jsonKey"
                        }],
                        groupBy: [{
                            table: "memoValues",
                            name: "jsonKey"
                        }],
                    },
                },
                processHistory: {
                    backgroundColor: ["#22B07D", "#D91828"],
                    requestData: {
                        table: "ApprovalHistory",
                        aggregations: [
                            {
                                function: "count",
                                column: {
                                    table: "ApprovalHistory",
                                    name: "processName"
                                },
                            },
                        ],
                        columns: [{
                            table: "ApprovalHistory",
                            name: "processName"
                        }],
                        groupBy: [{
                            table: "ApprovalHistory",
                            name: "processName"
                        }],
                        where: [
                            {
                                or: [
                                    {
                                        type: "equal",
                                        column: {
                                            table: "ApprovalHistory",
                                            name: "processName"
                                        },
                                        value: "generalProcess",
                                    },
                                    {
                                        type: "equal",
                                        column: {
                                            table: "ApprovalHistory",
                                            name: "processName"
                                        },
                                        value: "process-1",
                                    },
                                ],
                            },
                        ],
                    },
                },
            };
        },
    };
</script>

<style>
</style>