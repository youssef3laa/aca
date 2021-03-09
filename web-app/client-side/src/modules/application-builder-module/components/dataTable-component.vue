<template>
  <div class="datatable-component">
    <v-row class="justify-end">
      <v-col v-if="field.topActions == true && d.selected &&d.selected.length !=0" >
        <ActionsTopComponent :val="d.topActions"></ActionsTopComponent>
      </v-col>
      <v-col v-if="field.add == true" :cols="7">
        <button style="padding: 5px; margin: 20px" @click="handlAddButton()">
          <v-icon color="info">fas fa-plus</v-icon>
          <span> {{$t('add')}} </span>
        </button>
      </v-col>
      <v-col v-if="field.filter == true"  :cols="1">
        <button style="padding: 5px; margin: 20px">
          <v-icon>fas fa-filter</v-icon>
        </button>
      </v-col>
      <v-col  v-if="field.search == true"  :cols="4" class="table-search">
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="بحث"
          single-line
          hide-details
         
        ></v-text-field>
      </v-col>
    </v-row>

    <v-data-table
      :sort-by="field.sortBy"
      :sort-desc="field.sortDesc"
      :headers="d.headers"
      :items="filteredItems"
      v-model="d.selected"
      :options.sync="options"
      :server-items-length="d.url ? totalItems : -1"
      :loading="loading"
      :footer-props="footerProps"
      :search="search"
      :show-expand="!!(d.subTable || d.subHeaders)"
      :show-select="field.select"
      :single-select="field.singleSelect"
      :item-key="d.key"
      :item-class="itemRowStyle"
      color="blue"
      class="elevation-1"
    >
 
      <template v-slot:item.action="{ item }">
        <v-btn v-if="field.actions.length == 1"
                elevation="0"
                style="min-height: 24px"
                width="24px"
                v-on:click="handleAction(item, field.actions[0])">
          <v-icon v-if="field.actions[0] == 'edit'" style="font-size: medium"> far fa-edit </v-icon>
          <v-icon v-if="field.actions[0] == 'delete'" style="font-size: medium"> far fa-trash-alt </v-icon>
          <v-icon v-if="field.actions[0] == 'view'" style="font-size: medium"> fas fa-expand-arrows-alt </v-icon>
          <v-icon v-else style="font-size: medium"> {{field.actions[0].icon}} </v-icon>
        </v-btn>
        <v-menu v-if="field.actions.length > 1" offset-y left allow-overflow max-width="300">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              elevation="0"
              v-bind="attrs"
              v-on="on"
              style="min-height: 24px"
              width="24px"
            >
              <v-icon style="font-size: medium"> fas fa-ellipsis-h </v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-item-group>
              <v-list-item
                v-for="(i, index) in field.actions"
                :key="index"
                v-on:click="handleAction(item, i)"
              >
                <span v-if="i == 'edit'">
                  <v-list-item-title
                    style="color: black; font-weight: bold; font-size: small"
                  >
                    <v-icon style="color: black; font-size: small"
                      >far fa-edit</v-icon
                    >
                    <span style="margin: 3px"></span>
                    {{ $t(i) }}
                  </v-list-item-title>
                </span>

                <span v-else-if="i == 'delete'">
                  <v-list-item-title
                    style="color: black; font-weight: bold; font-size: small"
                  >
                    <v-icon style="color: black; font-size: small"
                      >far fa-trash-alt</v-icon
                    >
                    <span style="margin: 3px"></span>
                    {{ $t(i) }}
                  </v-list-item-title>
                </span>

                <span v-else-if="i == 'view'">
                  <v-list-item-title
                    style="color: black; font-weight: bold; font-size: small"
                  >
                    <v-icon style="color: black; font-size: small"
                      >fas fa-expand-arrows-alt</v-icon
                    >
                    <span style="margin: 3px"></span>
                    {{ $t(i) }}
                  </v-list-item-title>
                </span>

                <span v-else>
                  <v-list-item-title
                    style="color: black; font-weight: bold; font-size: small"
                  >
                    <v-icon style="color: black; font-size: small">{{
                      i.icon
                    }}</v-icon>
                    <span style="margin: 3px"></span>
                    {{ $t(i.name) }}
                  </v-list-item-title>
                </span>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-menu>
      </template>
      <template v-slot:expanded-item="{ item }">
        
<!--        <v-data-table v-if="d.subItems"-->
<!--            :headers="d.subHeaders"-->
<!--            :items="item[d.subItems]">-->
<!--        </v-data-table>-->

        <td :colspan="12" style="padding-bottom: 20px" v-if="d.subTable" >
          
          <data-table-component
                              :key="item[getProperty(item,d.subTable.value)]"
                              :field="item.subTable_ComponentOptions.field"
                              :val="item.subTable_ComponentOptions.val"
           >
          </data-table-component>
        </td>

        <!-- <span v-for="(subHeader,i) in d.subHeaders" :key="i"> -->
        <td v-else :colspan="12"
          style="margin:10px" >
          <div  v-for="(subHeader, i) in d.subHeaders" :key="i">
            <div class="top-bot-margins" style="color:#9E9E9E">{{ $t(subHeader.text) }}</div>

            <span v-if="isArray(item[subHeader.value])">
              <div v-for="(val, k) in item[subHeader.value]" :key="k" class="top-bot-margins" >{{ val[subHeader.items]}} </div>
            </span>
            
            <div v-else  class="top-bot-margins" >{{ item[subHeader.value] }} </div>
          </div>
        </td>

        
        <!-- </span> -->
      </template>
      <template   v-slot:[`item.${field.selectable}`]="{ item }">
        <v-checkbox
           color="#07689F"
          :ripple="false"
          :value="false" 
          v-model="item[field.selectable]"

        ></v-checkbox>
<!--         <span>{{item}}</span>-->
      </template>
            <!-- <template  v-for="(selectable,index) in field.selectables" v-slot:[`item.${selectable}`]="{ item }">
        <v-simple-checkbox
         :key="index"
           color="#07689F"
          :ripple="false"
          v-model="item[selectable]"
        ></v-simple-checkbox>
        <span
         :key="index"
        >{{item}}</span>
      </template> -->
       <template v-for="(image,index) in field.images"  v-slot:[`item.${image}`]="{ item }">
              <v-img :key="index" :src="item[image]" :alt="'signature'" class="thumbnail"></v-img>
          </template>


      <template v-slot:header.data-table-select="{props,on}">
        <v-simple-checkbox color="#07689F" :ripple="false" v-bind="props" v-on="on"></v-simple-checkbox>
      </template>

      <template v-slot:item.data-table-select="{isSelected,select}">
        <v-simple-checkbox color="#07689F" :ripple="false" :value="isSelected" @input="select($event)"></v-simple-checkbox>
      </template>
    </v-data-table>


  </div>
</template>
<script>
import http from "../../core-module/services/http";
import ActionsTopComponent from "./actions-top-component"
export default {
  name: "DataTableComponent",
  components:{ActionsTopComponent},
  data() {
    return {
      search: "",
      expanded: [],

      totalItems: this.val.data.length,
      d: this.val,
      loading: false,
      footerProps: {
        "items-per-page-options": [5, 10, 25],
        "show-first-last-page": true,
        "show-current-page": true,
        "items-per-page-text": '',
      },
      options: {},
      imageSrc:"https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ"
    };
  },
  computed:{
    filteredItems: function(){
          return this.d.data.filter((item)=>{
            let filterVal = true
            for(let index in this.d.filter){
              filterVal = filterVal && this.getProperty(item, this.d.filter[index].property).includes(this.d.filter[index].value)
            }
            return filterVal
          })
        }
  },
  watch: {
    'd.filter': function(){
      this.filteredItems()
    },
      search: function(){
          this.getDataFromApi({ page: 1, itemsPerPage: 10 })
      },
    options: {
      handler(event) {
        this.getDataFromApi(event);
      },
      deep: true,
    },
    val: {

        handler: function (newVal) {
            for (const key in newVal) {
                this.d[key] = newVal[key];
            }
            if (this.d.data && (this.d.data instanceof Array && this.d.data.length > 0)) {
                this.translateData();
            }
            if (this.d.headers) {
                this.translateHeaders();
            }
            // if (this.d.url) {
            //     this.getDataFromApi({page: 1, itemsPerPage: 10});
            // }
            else {
                this.loading = false;
            }
        }
    },
    'val.url':{
          deep:true,
        handler: function (newVal) {
            this.d.url=newVal;
            // for (const key in newVal) {
            //     this.d[key] = newVal[key];
            // }
            // if (this.d.data && (this.d.data instanceof Array && this.d.data.length > 0)) {
            //     this.translateData();
            // }
            // if (this.d.headers) {
            //     this.translateHeaders();
            // }
            // if (this.d.url) {
            if(newVal !== undefined)
                this.getDataFromApi({page: 1, itemsPerPage: 10});
            // } else {
            //     this.loading = false;
            // }
        }
    },

    'd.selected':function(){
      this.$observable.fire(this.field.name+"_"+"selected",this.d.selected)
    }
  },
  methods: {
    itemRowStyle: function(item){
      if(this.val.styleType=="goalsTable" && item.isTraditional){
        return 'row-item'
      }
    },
    updateData: function(){
      if(this.d.data instanceof Array){
        for(let item in this.d.data){
          if(this.d.subTable){
            this.d.data[item].subTable_ComponentOptions = {
              field: this.d.subTable.options,
              val: this.updateSubTableModel(this.d.data[item])
            }
          }
        }
      }
      this.translateData()
    },
    updateSubTableModel: function(item){
      let model = { headers: [], data: [], parent: item }
      if(this.d.subTable.model){
        for(let key in  this.d.subTable.model){
          model[key] = this.d.subTable.model[key]
        }
        if(model.url){
          if(this.d.subTable.value){
             let property = this.getProperty(item, this.d.subTable.value)
             model.url = model.url.replace(/\$\d+/,property)
          }
        }else{
          if(this.d.subTable.value){
             let property = this.getProperty(item, this.d.subTable.value)
             model.data = property;
          }
        }
      }
      return model
    },
    handlAddButton() {
      let item = {};
      for(let key in this.d.parent){
        if(key == "subTable_ComponentOptions")continue
        item[key] = this.d.parent[key];
      }
      this.$observable.fire(this.field.name + "_add", { item });
    },
    isArray(item){
      return item instanceof Array;
    },
    handleAction(obj, actionName) {
      if (actionName instanceof Object) actionName = actionName.name;
      //   console.log(item)
      //   console.log(actionName)
      let item = {};
      for(let key in obj){
        if(key == "subTable_ComponentOptions")continue
        item[key] = obj[key];
      }
      item.parentTableRow = this.d.parent

      this.$observable.fire(this.field.name + "_" + actionName, { item });
    },
    getDataFromApi(event) {
      if (!this.d.url) {
        this.loading = false;
        return;
      }
      if(this.loading) return;

      this.loading = true;
      const page = event.page - 1;
      let limit = event.itemsPerPage;
      if (limit == -1) {
        limit = this.totalItems;
      }
      this.d.params = this.d.params ? this.d.params : {};
      let params = this.d.params;
      params.page = page;
      params.size = limit;
      if(this.field.search == true) params.search = this.search;
      const URL = this.d.url + "?" + new URLSearchParams(params).toString();
      http
        .get(URL)
        .then((response) => {
            // console.log(response)
            if(!response.data.data){
              this.d.data = []
              this.loading = false
              return
            }
            // eslint-disable-next-line no-prototype-builtins
            let dataToBePopulated = response?.data?.data?.hasOwnProperty("content") ? response.data.data.content : response.data.data ? response.data.data : [];
            // eslint-disable-next-line no-prototype-builtins
            this.totalItems = (response.data.metaInfo !== undefined&&response.data.metaInfo.hasOwnProperty("totalCount")) ? response.data.metaInfo.totalCount : response?.data?.data?.totalElements;
            // console.log(this.totalItems);
            this.d.data = dataToBePopulated;
            if (dataToBePopulated && dataToBePopulated.length > 0) {
                // this.translateData();
                this.updateData();
            }
            this.loading = false;
        })
        .catch((error) => {
          console.error(error);
          this.loading = false;
        });
    },
    translateData: function() {
      for (var key in this.d.data) {
        for (var i = 0; i < this.d.headers.length; i++) {
          if(this.d.headers[i].translate == false) continue
          this.translateProperty(this.d.data[key], this.d.headers[i].value);
        }
      }
    },
    translateHeaders: function() {
      for (var key in this.d.headers) {
        this.d.headers[key].text = this.$t(this.d.headers[key].text);
      }
    },
    getProperty: function(obj, prop){
      let parts = prop.split(".");

      if (Array.isArray(parts)) {
        let last = parts.pop(),
                l = parts.length,
                i = 1,
                current = parts[0];

        while (current && (obj = obj[current]) && i < l) {
          current = parts[i];
          i++;
        }

        if (obj) {
          return obj[last];
        }
      } else {
        throw "parts is not valid array";
      }
    },
    translateProperty: function(obj, prop) {
      let parts = prop.split(".");

      if (Array.isArray(parts)) {
        let last = parts.pop(),
          l = parts.length,
          i = 1,
          current = parts[0];

        while (current && (obj = obj[current]) && i < l) {
          current = parts[i];
          i++;
        }

        if (obj) {
          obj[last] = this.$t(obj[last]);
        }
      } else {
        throw "parts is not valid array";
      }
    },
  },
  created() {
      if (this.field.subscribe) {
          // console.log("subscribe");
          this.$observable.subscribe(this.field.subscribe, (data) => {
              if (data.type == "modelUpdate") {
                  var keys = Object.keys(data.model);
                  keys.forEach((key, index) => {
                      console.log(index);
                      this.d[key] = data.model[key];
                  });
              }
              if (this.d.data) {
                  // this.translateData();
                this.updateData();
              }
              // console.log(data);
          });
      }
      this.$observable.subscribe(this.field.name + "_refresh",  ()=> {
          this.getDataFromApi({page: 1, itemsPerPage: 10});
      })
  },
  mounted() {
    console.log(this.d);
    if (this.d.headers) {
      this.translateHeaders();
    }
    if (this.d.data) {
      // this.translateData();
      this.updateData();
    }
  },
  props: ["val", "field"],
};
</script>
<style scoped>
.v-btn {
  color: black;
  background: #eaeaea;
  padding: 0 5px !important;
  margin: 0 5px !important;
  height: 20px !important;
  min-width: 0 !important;
}
.top-bot-margins{
  margin-top: 6px;
  margin-bottom: 6px;
}
.thumbnail{
  max-width:40px;
  height: auto;
}
.thumbnail:hover{
  max-width:150px;
  position: absolute;
  z-index: 99;
  height: auto;
}
.decision-btn {
  display: flex;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  margin: 10px;
  height: 40px;
  padding: 10px;
  justify-content: center;
  border-radius: 5px;
  color: #0278ae;
  cursor:pointer;
}
.decision-btn i {
  margin-left: 10px;
}
.decision-btn-title {
  white-space: nowrap;
  overflow: hidden;
}
.datatable-component /deep/ .row-item td:first-child{
  border-right:12px solid #C70039 ; 
}
/* .dropDown-menu {
  background: #96969f !important;
} */

</style>
