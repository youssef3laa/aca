<template>
    <span>
        <AlertComponent ref="alertComponent"></AlertComponent>
        <v-card
            v-if="!pastSignaturesOnly"
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
                        <span v-t="'this-signature-belongs-to'"></span> {{
                            viceOrHead == 1 ? $t('chairmanOfCommision') : $t('viceChairmanOfCommision')
                        }}
                    </p>
                </v-alert>

                <span>
                    <div class="col-12 mt-2"
                         style="padding:0 !important; ">
                        <span v-if="!readonly">
                             <VueSignaturePad
                                 id="signature"
                                 ref="signaturePad"
                                 :options="options"
                                 height="600px"
                                 width="100%"/>
                        </span>

                        <span v-else>

                            <v-img :src="currentShownSignautre"
                                   height="600px"
                                   style="border: 2px solid #aaaaaa;
                                    border-radius: 5px;"
                                   width="100%">

                            </v-img>
                        </span>

                    </div>
                    <v-row v-if="!readonly" style="padding: 10px;justify-content: center">
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
                        <!--                    <v-btn color="#07689F"-->
                        <!--                           style="background-color: #f2f7fa; margin: 10px"-->
                        <!--                           text-->
                        <!--                           @click="save">{{ $t('save') }}-->
                        <!--                    </v-btn>-->
                    </v-row>
                </span>

            </div>
        </v-card>
        <v-card v-if="enterTxt"
                outlined>
            <v-alert icon="far fa-file-alt"
                     outlined
                     prominent
                     style="padding-bottom: 5px"
                     type="info">
                <p style="font-size: 16px; color: black">
                    <span v-t="'signatureText'"
                          style="font-size: 20px; color: #07689F"></span>
                    <br/>
                    <span v-t="'this-field-for-signature'"></span> {{
                        viceOrHead == 1 ? $t('chairmanOfCommision') : $t('viceChairmanOfCommision')
                    }}
                </p>
            </v-alert>
            <v-card-text style="padding-top: 0">
                <textAreaComponent :field="{ name: 'signatureEnter', label: 'signatureText' }"
                                   @update="onValueChange"></textAreaComponent>
            </v-card-text>
        </v-card>

        <v-card :flat="true"
                style="border-radius: 6px; ">
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

                                <v-col>
                                    <div style="margin-bottom: 12px;">تأشيرة السيد رئيس الهيئة</div>
                                    <div class="attachment-iframe"
                                         style="height:600px;">
                                        <div v-if="headImg">

                                            <v-img :src="headImg"
                                                   alt="headImg"
                                                   contain
                                                   height="600"
                                                   max-width="600"


                                            >
                                            </v-img>

                                        </div>

                                        <v-icon v-else
                                                color="#9e9e9e"
                                                style="margin: 10px 10px 0 0; padding: 5px 10px; font-size: 150px;height:600px;max-width: 600px;">
                                            mdi-file-image-outline
                                        </v-icon>
                                    </div>

                                </v-col>

                                <v-col>
                                    <div style="margin-bottom: 12px;">تأشيرة السيد نائب رئيس الهيئة</div>

                                    <div class="attachment-iframe"
                                         style="height:600px;">
                                        <div v-if="viceImg">


                                            <v-img :src="viceImg"
                                                   alt="viceImg"
                                                   contain
                                                   height="600"
                                                   max-width="600"
                                            >
                                            </v-img>
                                        </div>

                                        <v-icon v-else
                                                color="#9e9e9e"
                                                style="margin: 10px 10px 0 0; padding: 5px 10px; font-size: 150px;height:600px;max-width: 600px;">
                                            mdi-file-image-outline
                                        </v-icon>
                                    </div>
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
                            <v-card-text style="color: #1A1A2E">
                                {{ headTxt }}
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
                                {{ viceTxt }}
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
import textAreaComponent from "../../application-builder-module/components/textarea-component"
import debounce from "debounce";

export default {
    components: {OpinionsTable, textAreaComponent},
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
            displayName: null,
            valid: null,
            currentShownSignautre: null,
            initializeDebounce: null
        }
    },
    mixins: [signatureMixin, userMixin],
    props: ['incomingEntityId', "viceOrHead", "requestId", 'readonly', 'enterTxt', 'pastSignaturesOnly', 'field'],
    methods: {
        onValueChange: function (obj) {
            this.$emit('update', {
                name: this.field.name,
                value: obj.value,
                key: "signatureTxt",
                type: 'inputChange',
            })
        },
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

                return this.createSignature({
                    data,
                    signatureDate: new Date().toISOString().split("T")[0],
                    viceOrHead: this.viceOrHead,
                    incomingEntityId: this.incomingEntityId
                });
                // this.$refs.alertComponent._alertSuccess({
                //     message: 'saveSignatureSuccess',
                // }, async () => {
                //     await this.initializeSignatures();
                // })
            } catch (e) {
                this.$refs.alertComponent._alertSuccess({
                    type: "error",
                    message: 'something went wrong',
                })
                console.error(e);
            }
        },
        async update(signatureEntityId) {
            const {isEmpty, data} = this.$refs.signaturePad.saveSignature()
            console.log(isEmpty);
            return await this.updateSignature({
                file: data,
                id: signatureEntityId,
                viceOrHead: this.viceOrHead
            }, true);
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
            if (!this.incomingEntityId) return;
            try {
                let firstSignatureResponse = null;
                this.loading = true;
                let getAllSignaturesResponse = await this.getAllSignatures(this.incomingEntityId);
                this.signatures = getAllSignaturesResponse.data.data;
                if (this.signatures instanceof Array && this.signatures.length > 0) {
                    firstSignatureResponse = await this.loadSignature(this.signatures[0].id);
                }
                this.loading = false;
                if (firstSignatureResponse) {
                    if (this.readonly) {
                        if (!this.pastSignaturesOnly) {
                            let imgKey;
                            if (this.viceOrHead == 1) {
                                imgKey = 'signatureHeadImg';
                            } else if (this.viceOrHead == 2) {
                                imgKey = 'signatureViceImg';
                            }
                            this.currentShownSignautre = "data:image/png;base64," + firstSignatureResponse.data.data[imgKey];
                            // this.$refs.signaturePad.fromDataURL("data:image/png;base64," + firstSignatureResponse.data.data[imgKey]);
                            // this.$refs.signaturePad.lockSignaturePad();
                            // this.$refs.signaturePad.resizeCanvas();
                            // console.log(this.$refs.signaturePad.);
                        }

                    }
                }


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
                return readSignatureResponse;
            } catch (e) {
                console.error(e);
            }
        },
        // redrawCanvasAfterResize: function () {
        //     debounce(() => {
        //         this.$refs.signaturePad.fromDataURL(this.currentShownSignautre)
        //     }, 50)
        // }
    },

    watch: {
        incomingEntityId: function () {
            if (!(this.loading))
                this.initializeSignatures()
        }

    },
    computed: {
        oldSignatures: function () {
            if (Object.prototype.hasOwnProperty.call(this.field, 'oldSignatures')) {
                return this.field.oldSignatures
            }
            return true
        },
    },
    // created() {
    //     let vueContext = this;
    //     window.addEventListener('resize', vueContext.redrawCanvasAfterResize)
    // },
    beforeDestroy() {
        this.initializeDebounce.clear();
    },
    async mounted() {
        this.initializeDebounce = debounce(async () => {
            console.log("initializeDebounce outside");
            if (!(this.loading)) {
                console.log("initializeDebounce inside");
                this.initializeDebounce.clear();
                await this.initializeSignatures();

            }
        }, 200);
        console.log("signature-draw-mounted");

        let userDetails = await this.getUserDetails()
        this.displayName = userDetails.displayName

        for (let i = 0; this.tabs && i < this.tabs.length; i++) {
            if (this.tabs[i].isActive) {
                this.selectedTab(this.tabs[i])
            }
        }


        this.$observable.subscribe("save-signature", async (obj) => {
            let status = false, signatureEntityId;
            if (!(this.$refs.signaturePad.isEmpty())) {
                if (obj.signatureEntityId) {
                    let signatureResponse = await this.update(obj.signatureEntityId);
                    console.log(signatureResponse)
                } else {
                    let signatureSaveResponse = await this.save();
                    console.log("signatureSaveResponse", signatureSaveResponse);
                    signatureEntityId = signatureSaveResponse.data.data.id;
                }
                status = true;
            }
            if (obj.callback) obj.callback({status, signatureEntityId});
        })

        this.$observable.subscribe("refreshSignatures", this.initializeDebounce);
    }
    ,
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
