<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
        <AlertComponent ref="alertComponent"></AlertComponent>
    </v-container>
</template>

<script>
    import http from "../../core-module/services/http"
    import formPageMixin from "../../../mixins/formPageMixin";
    import AppBuilder from "../../application-builder-module/builders/app-builder";

    export default {
        name: "lookup-page",
        mixins: [formPageMixin],
        components: {
            AppBuilder
        },
        data() {
            return {
                app: {},
                model: {},
                request: undefined
            };
        },
        async created() {
            this.request = await this.getRequest()
            this.loadForm("lookup-page", this.formLoaded)
            this.handleLookupEvents()
            this.handleOpenModals()
        },
        methods: {
            handleOpenModals: function (){
                this.$observable.subscribe("lookupTable_add", (obj) => {
                    let data = {
                        categoryId: obj.item.id,
                        category: obj.item.arValue,
                        modalTitle: "addLookup",
                        action: ['add']
                    }
                    this.$refs.appBuilder.setModelData("addLookupModal", data)
                    this.$observable.fire("addLookupModal", {
                        obj: data
                    })
                });
                this.$observable.subscribe("lookupTable_edit", (data) => {
                    console.log(data)
                    data.item.category = data.item.parentTableRow.arValue
                    data.item.modalTitle = "editLookup"
                    data.item.action = ["edit"]
                    delete data.item.parentTableRow
                    this.$refs.appBuilder.setModelData("editLookupModal", data.item)
                    this.$observable.fire("editLookupModal", {
                        obj: data.item,
                    })
                })
                this.$observable.subscribe("categoryTable_add", () => {
                    let data = {
                        modalTitle: "addCategory",
                        action: ["add"]
                    }
                    this.$refs.appBuilder.setModelData("addCategoryModal", data)
                    this.$observable.fire("addCategoryModal", {
                    })
                });
                this.$observable.subscribe("categoryTable_edit", (data) => {
                    data.item.modalTitle = "editCategory"
                    data.item.action = ["edit"]
                    this.$refs.appBuilder.setModelData("editCategoryModal", data.item)
                    this.$observable.fire("editCategoryModal", {
                        obj: data.item,
                    })
                })
            },
            handleLookupEvents: function () {
                this.$observable.subscribe("addLookupModal_add", (obj) => {
                    let item = obj.obj;
                    item.parentId = 0;
                    item.type = 2;
                    delete item.category;
                    delete item.modalTitle;
                    delete item.action;

                    http.post("/lookup/create", item).then(() => {
                        this.$observable.fire("lookupTable_refresh");
                        this.$refs.alertComponent._alertSuccess({
                            type: "success",
                            message: "initiateSuccess"
                        })
                    }).catch(() => {
                        this.$refs.alertComponent._alertSuccess({
                            type: "error",
                            message: "systemError"
                        })
                    });
                });
                this.$observable.subscribe("editLookupModal_edit", (obj) => {
                    let item = obj.obj;
                    let id = item.id;
                    delete item.category
                    delete item.modalTitle;
                    delete item.action;
                    http.put("/lookup/update/" + id, item).then(() => {
                        this.$observable.fire("lookupTable_refresh");
                        this.$refs.alertComponent._alertSuccess({
                            type: "success",
                            message: "addSuccess"
                        })
                    }).catch(() => {
                        this.$refs.alertComponent._alertSuccess({
                            type: "error",
                            message: "systemError"
                        })
                    });
                });
                this.$observable.subscribe("addCategoryModal_add", (obj) => {
                    let item = obj.obj;


                    item.type = 1
                    item.parentId = 0
                    delete item.modalTitle;
                    delete item.action;
                    http.post("/lookup/create", item).then(() => {
                        this.$observable.fire("categoryTable_refresh");
                        this.$refs.alertComponent._alertSuccess({
                            type: "success",
                            message: "addSuccess"
                        })
                    }).catch(() => {
                        this.$refs.alertComponent._alertSuccess({
                            type: "error",
                            message: "systemError"
                        })
                    });
                });
                this.$observable.subscribe("editCategoryModal_edit", (obj) => {
                    let item = obj.obj;
                    let id = item.id;

                    delete item.modalTitle;
                    delete item.action;
                    http.put("/lookup/update/" + id, item).then(() => {
                        this.$observable.fire("categoryTable_refresh");
                        this.$refs.alertComponent._alertSuccess({
                            type: "success",
                            message: "editSuccess"
                        })
                    }).catch(() => {
                        this.$refs.alertComponent._alertSuccess({
                            type: "error",
                            message: "systemError"
                        })
                    });
                });
            }
        }
    };
</script>