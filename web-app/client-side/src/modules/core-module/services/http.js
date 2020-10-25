import axios from 'axios'
import config from '../../../config/config'

const res = axios.create({  
    baseURL: config.API_URL,
    headers: {
        "X-TenantID" : "TenantOne"
    }
});  

export default {
    
    getAll(){
        /**@TODO check if logged in  */
        return res.get('customers', {  
            transformResponse: [function (data) {  
              return JSON.parse(data);  
            }]  
        })
    } 
    
}