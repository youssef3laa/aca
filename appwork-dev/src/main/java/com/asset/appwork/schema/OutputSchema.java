package com.asset.appwork.schema;

import lombok.Data;

import java.util.List;

@Data
public class OutputSchema {
    String taskId;
    String process;
    String stepId;
    String assignedCN;
    Boolean breakProcess;
    String subBP;
    String decision;
    String comment;
    String code;
    String entityName;
    String entityId;
    String page;
    String processId;
    String parentHistoryId;
    String roleFilter;
    Boolean addApproval;
    List<String> assignees;

    public void setStepId(String stepId){
        this.breakProcess = stepId.equals("end");
        this.stepId = stepId;
    }

    public String getXML(){
        return  "<assignedCN>\n"+removeNull(this.assignedCN)+"</assignedCN>\n"+
                "<stepId>\n"+removeNull(this.stepId)+"</stepId>\n"+
                "<process>\n"+removeNull(this.process)+"</process>\n"+
                "<subBP>\n"+removeNull(this.subBP)+"</subBP>\n"+
                "<entityName>\n"+removeNull(this.entityName)+"</entityName>\n"+
                "<entityId>\n"+removeNull(this.entityId)+"</entityId>\n"+
                "<parentHistoryId>\n"+removeNull(this.parentHistoryId)+"</parentHistoryId>\n"+
                "<page>\n"+removeNull(this.page)+"</page>\n"+
                "<decision>\n"+removeNull(this.decision)+"</decision>\n"+
                "<comment>\n"+removeNull(this.comment)+"</comment>\n"+
                "<roleFilter>\n"+removeNull(this.roleFilter)+"</roleFilter>\n"+
                "<breakProcess>\n"+removeNull(this.breakProcess)+"</breakProcess>\n"+
                "<addApproval>\n"+removeNull(this.addApproval)+"</addApproval>\n"+
                "<extraData>\n"+createExtraData()+"</extraData>\n";
    }

    public String getXMLWithNameSpace(){
        return "<ACA_ProcessRouting_OutputSchemaFragment xmlns=\"http://schemas.cordys.com/\">"+
                    getXML()+
                "</ACA_ProcessRouting_OutputSchemaFragment>\n";
    }

    private String createExtraData(){
        String extraData = "";
        if(assignees != null){
            for(String assignee: assignees){
                extraData += "<assignee>\n"+assignee+"</assignee>\n";
            }
        }
        return extraData;
    }

    public String getProcessFilePath(String basePath){
        return basePath + "\\" + this.process + ".json";
    }

    private String removeNull(Object object){
        if(object == null) return "";
        return object.toString();
    }
}
