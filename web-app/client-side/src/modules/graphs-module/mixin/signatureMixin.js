import axios from "axios";
import http from "../../core-module/services/http";

export default {
    methods: {
        async createSignature({
                                  data, signatureDate, viceOrHead, incomingEntityId
                              }) {
            // Covnvert base64 to blob
            const base64ToBlobResponse = await axios({
                url: `data:image/png;base64,${data.split(",")[1]}`,
                responseType: "blob",
            });

            //
            const formData = new FormData();


            formData.append("signatureDate", signatureDate);
            formData.append("viceOrHead", viceOrHead);
            formData.append("incomingEntityId", incomingEntityId);
            formData.append("file", base64ToBlobResponse.data);

            return http.post("/signature", formData);
        },
        readSignature(signatureEntityId) {
            return http.get("/signature/" + signatureEntityId);
        },
        getAllSignatures(incomingEntityId) {
            return http.get("/signature/all/" + incomingEntityId);
        }


    }
};
