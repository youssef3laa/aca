import axios from 'axios'
import config from '../../../config/config'

const REQUEST = axios.create({  
    baseURL: config.API_URL,
    headers: {
        'Content-Type': 'application/json',
        "X-TenantID" : "TenantOne"
    }
});  

export default {

    post(url, data){
        return REQUEST.post(url, data)
    },

    get(url){
        return REQUEST.get(url)
    },

    handleError(error){
        var res = () => (error.data) ? error.data :  error.response;
        
        if(!res()) return "CANNOT_CONNECT_TO_SERVER"

        return (res().status == 400)? "NOT_FOUND" : (res().data)? res().data.error.message : res().status
        
    },
    
    getAll(){
        /**@TODO check if logged in  */
        return REQUEST.get('customers', {  
            transformResponse: [function (data) {  
              return JSON.parse(data);  
            }]  
        })
    } 
    
}