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
    async downloadFromCS(documentId) {
      const request = await http.get(`/document/download/${documentId}`);
      return request.data.data;
    },
    async getSubNodes(parentId) {
      return await http.get(`/document/list/${parentId}`);
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
