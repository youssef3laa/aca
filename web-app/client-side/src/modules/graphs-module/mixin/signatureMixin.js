import axios from "axios";
import http from "../../core-module/services/http";

export default {
  methods: {
    async uploadToCS(base64, parentId) {
      // Covnvert base64 to blob
      const base64ToBlobResponse = await axios({
        url: `data:image/png;base64,${base64.split(",")[1]}`,
        responseType: "blob",
      });

      //
      const formData = new FormData();
      formData.append("parentId", parentId);
      formData.append("name", new Date().getTime() + ".png");
      formData.append("file", base64ToBlobResponse.data);

      return await http.post("/document/upload", formData);
    },
    async downloadFromCS(documentId) {
      const request = await http.get(`/document/download/${documentId}`);
      return request.data.data;
    },
    async getSubNodes(parentId) {
      return await http.get(`/document/list/${parentId}`);
    },
  },
};
