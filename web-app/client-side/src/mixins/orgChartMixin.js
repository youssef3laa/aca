import http from "../modules/core-module/services/http"

export default {
    methods: {
        getParentDetails: async function(){
            try {
                let response = await http.get("org/group/up")
                if (response.data.data instanceof Array && response.data.data.length > 0){
                    return response.data.data[0]
                }else if (response.data.data.name) {
                    return response.data.data
                }else{
                    return null
                }
            }
            catch (error) {
                console.log(error)
            }
        },
        getGroupsByUnitCodes: async function(unitCodes){
            try {
                let response = await http.get("org/group/findByUnitCodes/"+unitCodes+","+unitCodes)
                if (response.data.data instanceof Array && response.data.data.length > 0){
                    return response.data.data
                }else {
                    return null
                }
            }
            catch (error) {
                console.log(error)
            }
        },
        getHeadRoleByUnitCode: async function(unitCode){
            try {
                let response = await http.get("org/group/findByUnitCodes/"+unitCode)
                if (response.data.data instanceof Array && response.data.data.length > 0){
                    for(let key in response.data.data) {
                        if(response.data.data[key].type === "HEAD"){
                            return response.data.data[key]
                        }
                    }
                    return null
                }else {
                    return response.data.data
                }
            }
            catch (error) {
                console.log(error)
            }
        }
    }
}