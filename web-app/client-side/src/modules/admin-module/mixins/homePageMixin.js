import http from "../../core-module/services/http";
import router from "../../../router";
import HomePage from "../views/home-page";
import HomePageSecretary from "../views/home-page-secretary";
import HomePageTechnicalOfficeHead from "../views/home-page-technical-office-head";
import HomePageTechnicalOfficeSectorHead from "../views/home-page-technical-office-sector-head";

import HomePageCOC from "../views/home-page-COC"
export default {
    methods: {
        appendHomeRoutes: async function(){
            if(!localStorage.getItem("user")) return
            try {
                let response = await http.get("user/details")
                let userDetails = response.data.data
                console.log(userDetails)

                let homeComponent = HomePage;

                if(userDetails.groups[0]){
                    let group = userDetails.groups[0]
                    let unitCode = userDetails.groups[0].unit.unitCode
                    if(unitCode == "COC" && group.type === "HEAD") homeComponent = HomePageCOC
                    else if(unitCode == "COC"  && group.type === "SECRETARY")homeComponent = HomePageSecretary
                    else if((unitCode == "TVA" || unitCode == "TCA") && group.type === "HEAD") homeComponent = HomePageTechnicalOfficeHead
                    else if((unitCode == "TVS" || unitCode == "TCS") && group.type === "HEAD") homeComponent = HomePageTechnicalOfficeSectorHead
                }
                console.log(router)
                router.addRoutes([{
                    path: '/home',
                    name: 'HomePage',
                    component: homeComponent
                }])
            }
            catch (error) {
                console.log(error)
            }
        }
    }
}