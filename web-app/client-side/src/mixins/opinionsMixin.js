export default {

    methods: {
        createOpinionTableModel(requestId) {
            return {
                url: "opinion/"+ requestId,
                key:"7amada",
                headers: [
                    {
                        text: "name",
                        align: "start",
                        value: "displayName",
                    },
                    {
                        text: "theEntity",
                        value: "unitName",
                    },
                    {
                        text: "date",
                        value: "opinionDate",
                    }
                ],
                subHeaders:[{
                    text:"opinion",
                    value:"opinion"
                }],
                data: []
            }
        }
    }
}
