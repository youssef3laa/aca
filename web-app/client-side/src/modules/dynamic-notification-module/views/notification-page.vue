<template>
  <v-container fluid>
    <span>الإخطارات</span>
    <v-data-iterator
      :items="notifications"
      item-key="name"
      :items-per-page="4"
      disable-filtering
      disable-sort
    >
      <template v-slot:default="{ items }">
        <v-card>
          <v-row
            v-for="item in items"
            :key="item.NotificationId"
            class="pl-4 mt-0"
            v-on:mouseover="mouseOver(item)"
          >
            <v-col :cols="9" style="padding : 5px 25px 0 0">
              <h4>{{ item.Subject }}</h4>
            </v-col>
            <v-col :cols="3" style="padding : 5px 25px 0 0">
              {{ item.DeliveryDate }}
            </v-col>
            <v-col>
              <v-col :cols="12">
                <v-list dense>
                  <v-list-item>
                    <v-list-item-content>{{
                      item.NotificationData.ApplicationData
                        .ACA_DynamicNotification_InputSchemaFragment
                        .requestStatus
                    }}</v-list-item-content>
                  </v-list-item>
                </v-list>
              </v-col>
              <v-divider></v-divider>
            </v-col>
          </v-row>
        </v-card>
      </template>
    </v-data-iterator>
  </v-container>
</template>

<script>
import http from '../../core-module/services/http'
export default {
  data: () => ({
    notifications: [],
    hover: false,
    counter: 0,
  }),
  mounted() {
    http.get('notification/get/all').then((response) => {
      this.notifications = response.data.data
      console.log(this.notifications)
      for (let i = 0; i < response.data.data.length; i++) {
        if (!response.data.data[i].ReadFlag) {
          this.counter += 1
          console.log(this.counter)
        }
      }
      this.$observable.fire('notificationCounter', this.counter)
    })
  },
  methods: {
    mouseOver: function(item) {
      http.get('notification/read/' + item.NotificationId).then(() => {
        // console.log(response)
      })
    },
  },
}
</script>

<style>
.active {
  background: green;
}
</style>
