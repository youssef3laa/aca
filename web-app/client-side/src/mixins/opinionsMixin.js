export default {

    methods: {
        createOpinionTableModel(requestId) {
            return {
                url: "opinion/"+ requestId,
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