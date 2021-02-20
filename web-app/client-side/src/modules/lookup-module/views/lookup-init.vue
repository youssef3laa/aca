<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
    </v-container>
</template>

<script>
    import http from "@/modules/core-module/services/http"
    import formPageMixin from "../../../mixins/formPageMixin";
    import AppBuilder from "../../application-builder-module/builders/app-builder";

    export default {
        name: "lookup-init",
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
            this.loadForm("lookup-init", this.formLoaded)
            this.handleLookupEvents()
        },
        methods: {
            handleLookupEvents: function () {
            this.$observable.subscribe("addLookupModal_addModal", (obj) => {
                let item = obj.obj;

                http.post("/lookup/create", item)
                .catch((response) => {
                alert(response);
                });
            });
            this.$observable.subscribe("editLookupModal_updateModal", (obj) => {
                let item = obj.obj;
                let id = item.id;

                // item = {
                // "category": item.category,
                // "key": item.key,
                // "arValue": item.arValue,
                // "enValue": item.enValue,
                // "parentId": item.parentId,
                // }

                http.put("/lookup/update/" + id, item)
                .catch((response) => {
                alert(response);
                });
            });
            this.$observable.subscribe("lookupTable_add", () => {
                this.$observable.fire("addLookupModal", {
                action: "add",
                })
            });
            // this.$observable.subscribe("lookupTable_edit", (data) => {
            //     this.$refs.appBuilder.setModelData("editLookupModal", data.item)
            //     this.$observable.fire("editLookupModal", {
            //     action: "edit",
            //     obj: data.item,
            //     })
            // })
            }
        }
    };
</script>