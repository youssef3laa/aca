import axios from "axios";
import http from "../../core-module/services/http";

export default {
  methods: {
    async uploadToCS(base64, parent_id) {
      // Covnvert base64 to blob
      const base64ToBlobResponse = await axios({
        url: `data:image/png;base64,${base64.split(",")[1]}`,
        responseType: "blob",
      });

      //
      const formData = new FormData();
      formData.append("parent_id", parent_id);
      formData.append("name", new Date().getTime() + ".png");
      formData.append("file", base64ToBlobResponse.data);

      return await http.post("/document/upload", formData);
    },
    async thumbnail(signatureList) {
      // const request = await http.get(`/document/download/${documentId}`);
      // return request.data.data;
      const token = await http.post(
        "http://45.240.63.94:8081/otdsws/rest/authentication/credentials",
        {
          userName: "admin",
          password: "Asset99a",
          ticketType: "OTDSTICKET",
        }
      );
      const thumbnail = signatureList.map((signature) => {
        return {
          id: signature["data"]["properties"]["id"],
          date: signature["data"]["properties"]["create_date"],
          src: `http://45.240.63.94/otcs/cs.exe?func=thumbnail.fetchthumbnail&nodeid=${signature["data"]["properties"]["id"]}&verNum=1&verType=otthumb&pageNum=1&OTDSTicket=${token.data.ticket}`,
        };
      });
      return thumbnail;
    },
    async getSubNodes(parentId) {
      const request = await http.get(`/document/list/${parentId}`);
      return request.data.data;
    },

    async createFolder(parent_id, name) {
      const formData = new FormData();
      formData.append("parent_id", parent_id);
      formData.append("name", name);

      const request = await http.post("/document/create/folder", formData);
      return request.data.data;
    },
  },
};
