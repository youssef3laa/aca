<template>
    <span>
        <AlertComponent ref="alertComponent"></AlertComponent>
        <v-card v-if="readonly !== true"
                :flat="true"
                style="margin: 0 0 40px 0; border: thin solid rgba(0, 0, 0, 0.12);">
            <div class="container">
                <v-alert icon="mdi-draw"
                         outlined
                         prominent
                         type="info">
                    <p style="font-size: 16px; color: black">
                        <span style="font-size: 20px; color: #609ec1">
                            {{ $t('signature') }} </span><br/>
                        <span v-t="'this-field-for-notes'"></span> {{ displayName }}
                    </p>
                </v-alert>

                <span>
                   <div class="col-12 mt-2"
                        style="padding:0 !important; ">
                    <VueSignaturePad id="signature"
                                     ref="signaturePad"
                                     :options="options"
                                     height="600px"
                                     width="100%"/>
                </div>
                <v-row style="padding: 10px">
                    <v-btn color="#07689F"
                           style="background-color: #f2f7fa; margin: 10px"
                           text
                           @click="clear">{{ $t('clear') }}
                    </v-btn>
                    <v-btn color="#07689F"
                           style="background-color: #f2f7fa; margin: 10px"
                           text
                           @click="undo">{{ $t('cancel') }}
                    </v-btn>
                    <v-btn color="#07689F"
                           style="background-color: #f2f7fa; margin: 10px"
                           text
                           @click="save">{{ $t('save') }}
                    </v-btn>
                </v-row>
                </span>

            </div>
        </v-card>
        <v-card :flat="true" style="border-radius: 6px; ">
            <v-overlay :absolute="true"
                       :value="loading">
                <v-progress-circular indeterminate
                                     size="64"></v-progress-circular>
            </v-overlay>
            <div class="container">
                <v-expansion-panels v-model="panel"
                                    multiple
                                    style="position:relative; width: 100%;
                                    display: block;border-bottom-left-radius: 0;border-bottom-right-radius: 0;">

                    <v-expansion-panel>
                        <v-expansion-panel-header>
                            <v-row no-gutters>
                                <v-col cols="4">
                                    <v-alert icon="mdi-draw"
                                             outlined
                                             prominent
                                             type="info">
                                        <p style="font-size: 16px; color: black">
                                            <span style="font-size: 20px; color: #609ec1">
                                                {{ $t('oldSignatures') }}
                                            </span>
                                        </p>
                                    </v-alert>
                                    <span class="line"></span>
                                </v-col>
                                <v-col class="text--secondary"
                                       cols="8"></v-col>
                            </v-row>
                            <template v-slot:actions>
                            </template>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <span>
                                <v-sheet class="mx-auto"
                                         max-width="100%">
                                    <v-slide-group v-model="selected"
                                                   class="pa-4"
                                                   mandatory
                                                   show-arrows>
                                        <v-slide-item v-for="signature in signatures"
                                                      :key="signature.id"
                                                      v-slot="{ active, toggle }">

                                            <v-card :flat="true"
                                                    :style="active ? 'border: 2px solid #aaaaaa;' : ''"
                                                    class="ma-4"
                                                    height="45"
                                                    width="155"
                                                    @click="toggle();loadSignature(signature.id);">

                                                <v-card-text :style="!active ? 'border:2px solid #D1D1D1'  :''"
                                                             style="height: 100%">
                                                    {{ signature.signatureDate }}
                                                </v-card-text>
                                            </v-card>
                                        </v-slide-item>
                                    </v-slide-group>
                                </v-sheet>
                            </span>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
                <v-card :flat="true"
                        style=" border: thin solid rgba(0, 0, 0, 0.12); border-top: 0;width: 100%; border-top-right-radius: 0; border-top-left-radius: 0;">
                    <v-card flat
                            style="padding: 10px 0 5px 0;">
                        <span style="display: block;   background-color:#f8f8f8;    border:1px solid #e1e1e1;
                                   border-radius:6px; margin: 15px; padding: 5px;">
                            <button v-for="tab in tabs"
                                    :key="tab.id"
                                    :class="['tab-btn', { active: selectTab === tab }]"
                                    style="padding:15px;"
                                    @click="selectedTab(tab)">
                                {{ $t(tab.name) }}
                            </button>
                        </span>
                    </v-card>
                    <div v-if="selectTab.id == 1"
                         id="1">
                        <v-container>
                            <v-row style="justify-content: center">

                                <v-col class="attachment-iframe"
                                       style="margin:1px;height:600px;">

                                    <div v-if="headImg">

                                        <img :src="headImg"
                                             alt="headImg"
                                             style="height:600px;
width: 100%;
                                                   ">

                                    </div>

                                    <v-icon v-else
                                            color="#9e9e9e"
                                            style="margin: 10px 10px 0 0; padding: 5px 10px; font-size: 150px;height:600px;">
                                        mdi-file-image-outline
                                    </v-icon>
                                </v-col>

                                <v-col class="attachment-iframe"
                                       style="margin:1px;height:600px;">

                                    <div v-if="viceImg">
                                        <img :src="viceImg"
                                             alt="viceImg"
                                             style="height:600px;
width: 100%;">
                                    </div>

                                    <v-icon v-else
                                            color="#9e9e9e"
                                            style="margin: 10px 10px 0 0; padding: 5px 10px; font-size: 150px;height:600px;">
                                        mdi-file-image-outline
                                    </v-icon>
                                </v-col>
                            </v-row>
                        </v-container>
                    </div>
                    <div v-if="selectTab.id == 2"
                         id="2">
                        <v-card v-if="headTxt"
                                :flat="true"
                                class="mx-auto">


                            <v-card-subtitle class="v-card__title"
                                             style="color: #9E9E9E">
                                تأشيرة السيد رئيس الهيئة
                            </v-card-subtitle>
                            <v-card-text style="color: #1A1A2E" >
                                {{headTxt}}
                            </v-card-text>

                        </v-card>

                        <v-divider></v-divider>

                        <v-card v-if="viceTxt"
                                :flat="true"
                                class="mx-auto">
                            <v-card-subtitle class="v-card__title"
                                             style="color: #9E9E9E">
                                تأشيرة السيد نائب رئيس الهيئة
                            </v-card-subtitle>


                            <v-card-text style="color: #1A1A2E">
                                {{viceTxt}}

                            </v-card-text>

                        </v-card>


                    </div>


                    <div v-if="selectTab.id == 3"
                         id="3">
                        <opinions-table :requestId="requestId">

                        </opinions-table>
                    </div>
                </v-card>
            </div>
        </v-card>
    </span>
</template>
<script>
import signatureMixin from '../mixin/signatureMixin'
import userMixin from '../../../mixins/userMixin'
import OpinionsTable from "../../incoming-registration-module/views/opinions-table";

export default {
    components: {OpinionsTable},
    data() {
        return {
            signatures: [],
            panel: [0],
            tabs: [
                {
                    key: 'signature',
                    id: '1',
                    isActive: true,
                    name: 'التأشيرة',
                },
                {
                    key: 'signatureTxt',
                    id: '2',
                    isActive: false,
                    name: 'نص التأشيرة',
                },
                {
                    key: 'opinion',
                    id: '3',
                    isActive: false,
                    name: 'إبداء الرأي',
                },
            ],
            options: {
                penColor: 'black',
                onBegin: () => {
                    this.$refs.signaturePad.resizeCanvas()
                },
                // backgroundColor: 'rgb(255, 255, 255)'
            },

            selected: null,
            selectTab: {
                key: 'signature',
                id: '1',
                isActive: true,
                name: 'التأشيرة',
            },
            loading: false,
            headImg: null,
            viceImg: null,
            headTxt: null,
            viceTxt: null,
            displayName: null
        }
    },
    mixins: [signatureMixin, userMixin],
    props: ['incomingEntityId', "viceOrHead", "requestId", 'readonly', 'field'],
    methods: {
        selectedTab: function (tab) {
            this.selectTab = tab
            this.selectTab.isActive = true
            for (let i = 0; this.tabs && i < this.tabs.length; i++) {
                this.tabs[i].isActive = false
                // noinspection EqualityComparisonWithCoercionJS
                if (this.selectTab.id == this.tabs[i].id) {
                    this.tabs[i].isActive = true
                    this.selectTab.isActive = true
                }
            }
        },
        undo() {
            this.$refs.signaturePad.undoSignature()
        },
        async save() {
            const {isEmpty, data} = this.$refs.signaturePad.saveSignature()
            console.log(isEmpty)

            try {
                let createSignatureResponse = await this.createSignature({
                    data,
                    signatureDate: new Date().toISOString().split("T")[0],
                    viceOrHead: this.viceOrHead,
                    incomingEntityId: this.incomingEntityId
                });
                console.log(createSignatureResponse);
                this.$refs.alertComponent._alertSuccess({
                    message: 'saveSignatureSuccess',
                })
                await this.initializeSignatures();
            } catch (e) {
                this.$refs.alertComponent._alertSuccess({
                    type: "error",
                    message: 'something went wrong',
                })
                console.error(e);
            }
        },
        change() {
            this.options = {
                penColor: '#00f',
            }
        },
        resume() {
            this.options = {
                penColor: '#c0f',
            }
        },
        clear() {
            this.$refs.signaturePad.clearSignature()
        },
        initializeSignatures: async function () {
            try {
                let getAllSignaturesResponse = await this.getAllSignatures(this.incomingEntityId);
                this.signatures = getAllSignaturesResponse.data.data;
                if (this.signatures instanceof Array && this.signatures.length > 0) this.loadSignature(this.signatures[0].id);
            } catch (e) {
                console.error(e);
            }
        },
        loadSignature: async function (signatureEntityId) {
            try {
                let readSignatureResponse = await this.readSignature(signatureEntityId);
                let base64Start = "data:image/png;base64,";
                this.headImg = readSignatureResponse.data.data.signatureHeadImg != null ? base64Start + readSignatureResponse.data.data.signatureHeadImg : false;
                this.viceImg = readSignatureResponse.data.data.signatureViceImg != null ? base64Start + readSignatureResponse.data.data.signatureViceImg : false;
                this.headTxt = readSignatureResponse.data.data.signatureHeadTxt != null ? readSignatureResponse.data.data.signatureHeadTxt : false;
                this.viceTxt = readSignatureResponse.data.data.signatureViceTxt != null ? readSignatureResponse.data.data.signatureViceTxt : false;
                console.log(readSignatureResponse);
            } catch (e) {
                console.error(e);
            }
        }
    },

    watch: {
        incomingEntityId: function () {
            this.initialize()
        },
    },
    computed: {
        oldSignatures: function () {
            if (Object.prototype.hasOwnProperty.call(this.field, 'oldSignatures')) {
                return this.field.oldSignatures
            }
            return true
        },
    },

    async mounted() {
        let userDetails = await this.getUserDetails()
        this.displayName = userDetails.displayName
        this.$observable.subscribe('resizeCanvas', () => {
            console.log('canvas')
            this.clear()
        })

        for (let i = 0; this.tabs && i < this.tabs.length; i++) {
            if (this.tabs[i].isActive) {
                this.selectedTab(this.tabs[i])
            }
        }
        await this.initializeSignatures();
    },
}
</script>

<style>
#signature {
    /* border: double 3px transparent;
  border-radius: 5px;
  background-image: linear-gradient(white, white),
      radial-gradient(circle at top left, #4bc5e8, #9f6274);
  background-origin: border-box;
    background-clip: content-box, border-box; */
    background: var(--unnamed-color-ffffff) 0 0% no-repeat padding-box;
    background: #ffffff 0 0 no-repeat padding-box;
    box-shadow: 0 3px 6px #00000014;
    border: 2px solid #aaaaaa;
    border-radius: 5px;
    opacity: 1;
}

.v-alert--outlined {
    border: none !important;
}

.v-slide-item--active {
    border: 2px solid #2d7fae !important;
}

.tab-btn {
    outline: none;
}

.active {
    /* border-bottom: 3px solid #0278ae !important; */
    border: 1px solid white;
    border-radius: 6px;
    background-color: white;
    color: #0278ae;
}
</style>
