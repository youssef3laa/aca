<template>
    <splitpanes style="height: auto;direction: ltr" class="default-theme">
        <pane style="direction: rtl">
            <AppBuilder v-show="sidebarItem == 'viewReceived'" ref="inbox" :app="app"/>
            <AppBuilder v-show="sidebarItem == 'viewSent'" ref="outbox" :app="app2"/>
        </pane>
        <pane style="height: auto; direction: rtl" max-size="14" min-size="10" size="14">
            <Sidebar :val="sidebarItems" @btnClicked="updateView"></Sidebar>
        </pane>
    </splitpanes>
</template>

<script>

    import router from "../../../router";
    import http from "../../core-module/services/http";
    import "splitpanes/dist/splitpanes.css";
    import {Splitpanes, Pane} from "splitpanes";
    import AppBuilder from "../builders/app-builder"
    import Sidebar from "../../application-builder-module/components/sidebar-component";
    import formPageMixin from "../../../mixins/formPageMixin"
    // import Topbar from "../../application-builder-module/components/topbar-component"

    export default {
        mixins: [formPageMixin],
        components: {Sidebar, Splitpanes, Pane, AppBuilder},
        props: ["val", "field"],
        data() {
            return {

                response: [],
                app: {},
                app2: {},
                sidebarItem: "viewReceived",
                sidebarItems: [
                    {
                        name: "الوارد",
                        notifications: 0,
                        icon: "fas fa-download",
                        action: "viewReceived"
                    },
                    {
                        name: "المرسل",
                        notifications: 0,
                        icon: "far fa-paper-plane",
                        action: "viewSent"
                    },
                ],
            };
        },
        methods: {
            viewTask(item) {
                try {
                    let taskId = item.item.TaskId,
                        page =
                            item.item.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.component;
                    router.push({
                        name: page,
                        params: {taskId: taskId},
                    });
                } catch (e) {
                    console.error(e);
                }
            },
            viewSentTask(item){
                try {
                    let requestId = item.item.requestId;
                    let page = item.item.readonlyComponent;
                    router.push({
                        name: page,
                        params: {requestId: requestId}
                    })
                } catch (e) {
                    console.error(e)
                }
            },
            fillInbox: function () {
                http.get('workflow/human/tasks').then((response) => {
                    console.log(response)
                    let data = response.data.data
                    console.log(data)

                    let approvals = [];
                    let modifications = [];
                    let opinions = [];
                    let assignments = [];
                    let internalMessages = [];

                    for (let key in data) {
                        switch (data[key].TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.caseType) {
                            case "modification":
                                modifications.push(data[key])
                                break
                            case "opinion":
                                opinions.push(data[key])
                                break
                            case "assignment":
                                assignments.push(data[key])
                                break
                            case "internalMessage":
                                internalMessages.push(data[key])
                                break
                            default:
                                approvals.push(data[key])
                        }
                    }
                    console.log(data)

                    this.sidebarItems[0].notifications = data.length

                    this.$refs.inbox.setTabValue("forApprovalTab", approvals.length + "")
                    this.$refs.inbox.setModelData("forApprovalTable", {taskTable: {data: approvals}});

                    this.$refs.inbox.setTabValue("modificationTab", modifications.length + "")
                    this.$refs.inbox.setModelData("modificationTable", {taskTable: {data: modifications}});

                    this.$refs.inbox.setTabValue("makeOpinionTab", opinions.length + "")
                    this.$refs.inbox.setModelData("makeOpinionTable", {taskTable: {data: opinions}});

                    this.$refs.inbox.setTabValue("assignmentsTab", assignments.length + "")
                    this.$refs.inbox.setModelData("assignmentsTable", {taskTable: {data: assignments}});

                    this.$refs.inbox.setTabValue("internalMessagingTab", internalMessages.length + "")
                    this.$refs.inbox.setModelData("internalMessagingTable", {taskTable: {data: internalMessages}});
                });
            },
            fillOutbox: function() {
                http.get('history/user/count').then((response) => {
                    if(!response.data.data) return;
                    this.sidebarItems[1].notifications = response.data.data
                });
            },
            updateView($event) {
                if ($event == "viewSent") {
                    this.sidebarItem = "viewSent";
                } else if ($event == "viewReceived") {
                    this.sidebarItem = "viewReceived";
                }
            },
        },
        mounted: function () {
            this.loadForm("inbox", this.fillInbox, "inbox")
            this.loadForm("outbox", this.fillOutbox, "outbox")

            this.$observable.subscribe("taskTable_view", (item) => {
                this.viewTask(item);
            });
            this.$observable.subscribe("outboxTable_view", (item) => {
                this.viewSentTask(item);
            });
            console.log(JSON.stringify(this.app))
        },
    };
</script>

<style></style>
