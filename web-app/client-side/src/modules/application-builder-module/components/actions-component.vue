<template>
    <v-col class="text-left"
           style="padding-top: 20px; padding-bottom:20px">
        <span v-for="(action, index) in actions"
              :key="index">
            <v-btn v-if="action == 'cancel'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="cancelStep">
                <v-icon color="error">far fa-times-circle</v-icon>
                <span style="margin: 3px"></span>
                <span v-t="cancelText"
                      style="font-weight: bold"></span>
            </v-btn>
            <v-btn v-if="action == 'save'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="saveStep">
                <v-icon color="info">far fa-save</v-icon>
                <span style="margin: 3px"></span>
                <span v-t="saveText"
                      style="font-weight: bold"></span>
            </v-btn>
            <v-btn v-if="action == 'complete'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="completeStep">
                <v-icon color="info">far fa-paper-plane</v-icon>
                <span style="margin: 3px"></span>
                <span v-t="sendText"
                      style="font-weight: bold"></span>
            </v-btn>

            <v-btn v-if="action == 'sendBackToAdministration'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendBackToAdministration">
                <v-icon color="#900">mdi-replay</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendBackToAdministration") }}</span>
            </v-btn>
            <v-btn v-if="action == 'sendBackToTheTechnicalOffice'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendBackToTheTechnicalOffice">
                <v-icon color="#900">mdi-replay</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendBackToTheTechnicalOffice") }}</span>
            </v-btn>
            <v-btn v-if="action == 'sendBackToVicePresident'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendBackToVicePresident">
                <v-icon color="#900">mdi-replay</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendBackToVicePresident") }}</span>
            </v-btn>
            <v-btn v-if="action == 'sendBackToPresident'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendBackToPresident">
                <v-icon color="red">mdi-replay</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendBackToPresident") }}</span>
            </v-btn>
              <v-btn v-if="action == 'saveTemporarily'"
                     :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                     color="white"
                     elevation="0"
                     height="52px"
                     @click="saveTemporarily">
                <v-icon color="info">far fa-save</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("saveTemporarily") }}</span>
            </v-btn>
            <v-btn v-if="action == 'sendForConfirmation'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendForConfirmation">
                <v-icon color="info">far fa-paper-plane</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendForConfirmation") }}</span>
            </v-btn>
            <v-btn v-if="action == 'sendToVicePresident'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendForConfirmation">
                <v-icon color="info">far fa-paper-plane</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendToVicePresident") }}</span>
            </v-btn>
            <v-btn v-if="action == 'doneViewing'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendForConfirmation">
                <v-icon color="info">fa fa-pen</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("doneViewing") }}</span>
            </v-btn>
            <!--change-->
            <v-btn v-if="action == 'sendToChairmanOfCommission'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendForConfirmation">
                <v-icon color="info">fa fa-pen</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendToChairmanOfCommission") }}</span>
            </v-btn>
            <v-btn v-if="action == 'sendToTheTechnicalOffice'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendToTheTechnicalOffice">
                <v-icon color="info">fa fa-pen</v-icon>
                <span style="margin: 3px"></span>
                <span
                    style="font-weight: bold">{{ $t("sendToTheTechnicalOffice") }}</span>
            </v-btn>

          <v-btn v-if="action == 'sendBackForConfirmation'"
                 :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                 color="white"
                 elevation="0"
                 height="52px"
                 @click="sendBackForConfirmation">
            <v-icon color="#900">mdi-replay</v-icon>
            <span style="margin: 3px"></span>
            <span
                style="font-weight: bold">{{ $t("sendBackForConfirmation") }}</span>
          </v-btn>
            <v-btn v-if="action == 'sendToOtherAdministration'"
                   :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
                   color="white"
                   elevation="0"
                   height="52px"
                   @click="sendToOtherAdministration">
                <v-icon color="info">far fa-paper-plane</v-icon>
            <span style="margin: 3px"></span>
            <span
                style="font-weight: bold">{{ $t("sendToOtherAdministration") }}</span>
          </v-btn>

<!--        <v-btn v-for="(item, index) in actions" v-else-->
<!--               :key="index"-->
<!--               :style="{marginLeft: (index === actions.length-1)? '0px':'5px', marginRight: (index === 0)? '0px':'5px'}"-->
<!--               color="white"-->
<!--               elevation="0"-->
<!--               height="52px"-->
<!--               @click="item.hasOwnProperty('callback') ? item.callback() : publishOnGivenKey(item)">-->
<!--            <v-icon :color="item.hasOwnProperty('color') ? item.color : 'info'">{{ item.icon }}</v-icon>-->
<!--            <span style="margin: 3px"></span>-->
<!--            <span style="font-weight: bold">{{ $t(item.text) }}</span>-->
<!--        </v-btn>-->


        </span>
    </v-col>
</template>

<script>
import router from "../../../router";

export default {
    props: ['actions'],
    data() {
        return {
            cancelText: 'cancel',
            saveText: 'save',
            sendText: 'send',
        }
    },
    methods: {
        publishOnGivenKey: function () {

        },
        sendBackForConfirmation: function () {
            this.$observable.fire('send-back-for-confirmation')

        },
        sendToOtherAdministration: function () {
            this.$observable.fire('send-to-other-administration')
        },
        completeStep: function () {
            this.$observable.fire('complete-step')
        },
        sendToTheTechnicalOffice: function () {
            this.$observable.fire('send-to-technical-office');
        },
        sendBackToPresident: function () {
            this.$observable.fire('send-back-to-president');
        },
        saveStep: function () {
            this.$observable.fire('save-step')
        },
        cancelStep: function () {
            router.push({name: 'HomePage'})
        },
        saveTemporarily: function () {
            this.$observable.fire('save-temporarily')
        },
        sendBackToAdministration: function () {
            this.$observable.fire('send-back-to-administration')
        },
        sendBackToVicePresident: function () {
            this.$observable.fire('send-back-to-vice-president')
        },
        sendForConfirmation: function () {
            this.$observable.fire('send-for-confirmation')
        },
        sendBackToTheTechnicalOffice: function () {
            this.$observable.fire('send-back-to-technical-office')
        },
    },
}
</script>

<style scoped>
.actions-contianer {
    margin: 15px 15px 15px 0;
    padding: 5px;
    background: #ffffff;
    border: 1px solid #f8f8f8;
    border-radius: 4px;
}
</style>
