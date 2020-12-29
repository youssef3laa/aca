package com.asset.appwork.schema;

import lombok.Data;

@Data
public class OutputSchema {
    String taskId;
    String process;
    String stepId;
    String assignedCN;
    String assignedType;
    Boolean breakProcess;
    String subBP;
    String decision;
    String comment;
    String code;
    String entityName;
    String entityId;
    String page;

    public String getAssignedType() {
        if(assignedCN.contains("cn=organizational users")){
            return "user";
        }else{
            return "role";
        }
    }

    public void setStepId(String stepId){
        if(stepId.equals("break")){
            this.breakProcess = true;
        }else{
            this.breakProcess = false;
        }
        this.stepId = stepId;
    }

    public String getXML(){
        return  "<assignedCN>"+removeNull(this.assignedCN)+"</assignedCN>\n"+
                "<assignedType>"+this.getAssignedType()+"</assignedType>\n"+
                "<stepId>"+removeNull(this.stepId)+"</stepId>\n"+
                "<process>"+removeNull(this.process)+"</process>\n"+
                "<subBP>"+removeNull(this.subBP)+"</subBP>"+
                "<entityName>"+removeNull(this.entityName)+"</entityName>"+
                "<entityId>"+removeNull(this.entityId)+"</entityId>"+
                "<page>"+removeNull(this.page)+"</page>"+
                "<decision>"+removeNull(this.decision)+"</decision>"+
                "<comment>"+removeNull(this.comment)+"</comment>"+
                "<breakProcess>"+removeNull(this.breakProcess)+"</breakProcess>\n";
    }

    public String getXMLWithNameSpace(){
        return "<ACA_ProcessRouting_OutputSchemaFragment xmlns=\"http://schemas.cordys.com/\">"+
                    getXML()+
                "</ACA_ProcessRouting_OutputSchemaFragment>\n";
    }

    public String getProcessFilePath(String basePath){
        return basePath + "\\" + this.process + ".json";
    }

    private String removeNull(Object object){
        if(object == null) return "";
        return object.toString();
    }
}
