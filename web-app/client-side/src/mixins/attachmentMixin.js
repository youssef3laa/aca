import Http from "../modules/core-module/services/http";

export default {
    methods: {
        setFileTypeOnFileUploaded: function ({categories, properties}) {
            let categoryValue;
            if (categories == undefined || categories.length == 0) {
                //shouldn't go here...
                properties.fileTypeValue = "لم يتم إدخاله بعد";
                return;
            }
            categoryValue = categories[0][this.categoryId + "_2"];

            if (categoryValue == -100) properties.fileTypeValue = this.$t('memoRandumFileType');
            else {
                let lookupObj = this.fileTypes.find((element) => element.value == categoryValue);
                properties.fileTypeValue = lookupObj?.text ?? "قيمة غير معرفة";
            }

        },
        openFileInBrave: async function ({fileId, verNum}, contextObj) {
            if(contextObj != undefined) this.toggleFileSelected(fileId, contextObj);
            let userToken;
            try {
                userToken = await Http.post("http://45.240.63.94:8081/otdsws/rest/authentication/credentials", {
                    "userName": "admin",
                    "password": "Asset99a",
                    "ticketType": "OTDSTICKET"
                });
                if (verNum) {
                    this.iframeOjbect.src =
                        'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&vernum=' + verNum + '&OTDSTicket=' + userToken.data.ticket;
                } else {
                    this.iframeOjbect.src =
                        'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
                }

            } catch (e) {
                console.log(e);
            }

        },
        deleteFile: async function (file) {

            let fileStatus = file.isActive;
            file = file.properties;
            if (file == undefined || file.id == undefined) return;
            try {
                await Http.delete('/document/delete/' + file.id);
                this.filesUploaded = this.filesUploaded.filter(element => element.properties.id != file.id);
                this.files = this.files.filter((fileVal) => fileVal.name != file.name);
                let attachmentSortId;
                this.attachmentSortList = this.attachmentSortList.filter(value => {
                    if (value.fileId != file.id) {
                        return true;
                    } else {
                        attachmentSortId = value.id;
                        return false
                    }
                })
                if (!attachmentSortId) await Http.delete('/document/sort/' + attachmentSortId);
                let tempArr = [];
                for (let i = 0; i < this.filesUploaded.length; ++i) {
                    let element = this.filesUploaded[i];
                    let attachmentSortElement = this.attachmentSortList.find(val => val.fileId == element.properties.id);
                    attachmentSortElement.position = i;
                    tempArr.push(attachmentSortElement);
                }
                this.updateMultipleAttachmentSortRecords(tempArr);
                if (fileStatus) {
                    this.iframeOjbect.src = "";
                }
            } catch (e) {
                console.error(e)
            }


        },
        listFiles: async function () {
            let nodesResponse
                , attachmentSortResponse;
            try {
                nodesResponse = await Http.get('/document/list/' + this.bwsId + '?fields=properties&fields=categories&where_type=-3');
                attachmentSortResponse = await Http.get('/document/sort', {
                    params: {
                        requestEntityId: this.requestEntityId,
                        bwsId: this.bwsId
                    }
                })
            } catch (e) {
                console.log(e);
            }
            if (!nodesResponse) return;
            nodesResponse = nodesResponse.data.data;
            this.attachmentSortList = attachmentSortResponse.data.data;


            nodesResponse.forEach((val) => {
                let attachmentSortElementObj;
                if ((attachmentSortElementObj = this.attachmentSortList.find(attachmentElement => attachmentElement.fileId == val.properties.id)) != null) {
                    val.properties.position = attachmentSortElementObj.position;
                    attachmentSortElementObj.exists = true;
                }
                //TODO move to function
                this.setFileTypeOnFileUploaded(val);
            });
            nodesResponse.sort((a, b) => (a.properties.position > b.properties.position) ? 1 : -1)
            let itemsToPost = [];
            nodesResponse.forEach((val, i) => {
                // noinspection EqualityComparisonWithCoercionJS
                if (val.properties.position == undefined) {
                    val.properties.position = i
                    let itemToPost = {
                        "fileId": val.properties.id,
                        "bwsId": this.bwsId,
                        "requestEntityId": this.requestEntityId,
                        "position": i
                    };
                    itemsToPost.push(itemToPost);

                }
            })
            if (itemsToPost.length > 0) {
                try {
                    let documentSortResponse = await Http.post('/document/sort/bulk', itemsToPost)
                    documentSortResponse.data.data.forEach(element => {
                        element.exists = true;
                        this.attachmentSortList.push(element)
                    });

                } catch (e) {
                    console.log(e);
                }
            }
            let attachmentSortListsToBeDeleted = this.attachmentSortList.filter(element => !element.exists);
            let ids = attachmentSortListsToBeDeleted.map(element => element.id).join(',');
            if (attachmentSortListsToBeDeleted.length > 0) {
                try {
                    await Http.delete('/document/sort/bulk/' + ids)
                } catch (e) {
                    console.log(e)
                }

            }
            this.filesUploaded.splice(0);

            nodesResponse.forEach((val) => {
                val.isActive = false;
                this.filesUploaded.push(val)
            });
        },
        updateAttachmentSortRecord: function (obj) {
            let itemToBeUpdated = this.attachmentSortList.find(val => val.fileId == obj.fileId);
            itemToBeUpdated.position = obj.position;
            Http.post('/document/sort/update', itemToBeUpdated)
                .then(response => console.log(response))
                .catch(reason => console.log(reason));
        },
        createAttachmentSortRecord: async function (arr) {
            if (!(arr instanceof Array) && arr != undefined) arr = [].concat(arr);
            if (arr instanceof Array && arr.length > 0) {
                console.log(arr);
                try {
                    let documentSortResponse = await Http.post('/document/sort/bulk', arr)
                    documentSortResponse.data.data.forEach(element => {
                        element.exists = true;
                        this.attachmentSortList.push(element)
                    });

                } catch (e) {
                    console.log(e);
                }
            }
        },
        updateMultipleAttachmentSortRecords: function (arr) {
            Http.post('/document/sort/update/bulk', arr)
                .then(() => console.log("finished updating position in backend"))
                .catch(reason => console.error(reason))
        },
        uploadFile: async function (obj) {
            console.log(obj);

            let tempObj = {};
            let formData = new FormData();
            formData.append('file', obj.file.file);
            formData.append('parent_id', this.bwsId)
            formData.append('name', obj.file.file.name)
            formData.append('category_id', this.categoryId)
            tempObj[this.categoryId + "_2"] = obj.file.fileTypeSelected;
            formData.append('categoriesMap', JSON.stringify(tempObj))
            Http.addHeader('Content-Type', 'multipart/form-data')
            try {

                let uploadDataResponse = await Http.post(`document/uploadAndSetCategory`, formData)
                if (uploadDataResponse.status == 200) {
                    this.files.splice(obj.index, 1);
                    // await this.listFiles();
                    uploadDataResponse = uploadDataResponse.data.data;
                    let toBeCreated = {
                        "fileId": uploadDataResponse.properties.id,
                        "bwsId": this.bwsId,
                        "requestEntityId": this.requestEntityId,
                        "position": this.filesUploaded.length
                    }
                    await this.createAttachmentSortRecord(toBeCreated);
                    this.filesUploaded.push(uploadDataResponse);
                    this.setFileTypeOnFileUploaded(uploadDataResponse);
                }
                // console.log(uploadDataResponse);
            } catch (e) {
                console.log(e);
            }
        },
        fillFilesArray: function (filesSource) {
            if (!filesSource) return;
            filesSource.forEach(element => {
                this.files.push({
                    file: element,
                    removeFile: (obj) => {
                        console.log(obj)
                        this.files.splice(obj.index, 1);
                    },
                    fileTypeSelected: null
                })
            })
        },
        openVersionsPopup: function (file) {
            console.log("openVersions popup === attachmentMixinjs");
            console.log(file.properties);
            this.versionsDialogState = true;
            this.selectedFile.nodeId = file.properties.id
            this.selectedFile.modalTitle = file.properties.name;
            // this.$observable.fire("openVersionsModal", file)
        }
        , onEnd: function () {
            let tempArr = [];
            for (let i = 0; i < this.filesUploaded.length; ++i) {
                let element = this.filesUploaded[i];
                let attachmentSortElement = this.attachmentSortList.find(
                    (val) => val.fileId == element.properties.id
                );
                attachmentSortElement.position = i;
                tempArr.push(attachmentSortElement);
            }
            this.updateMultipleAttachmentSortRecords(tempArr);
        },
        startDrag: function (evt, file) {
            evt.dataTransfer.dropEffect = "move";
            evt.dataTransfer.effectAllowed = "move";
            evt.dataTransfer.setData("itemID", file.properties.id);
        },
        // should be encapsulated and take all values from parameters
        toggleFileSelected: function (nodeId, contextObj) {
            let filesArray = this.filesUploaded;
            if (filesArray === undefined) filesArray = contextObj.filesUploaded;
            filesArray.forEach((element) => {
                element.isActive = element.properties.id === nodeId;
            })
        }
    }
}
