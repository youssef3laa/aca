package com.asset.appwork.schema;

import lombok.Data;

@Data
public class OutputSchema {
    String process;
    String stepId;
    String assignedCN;
    String assignedType;
    Boolean breakProcess;
    String subBP;
    String decision;
    String code;

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
        return  "<assignedCN>"+this.assignedCN+"</assignedCN>\n"+
                "<assignedType>"+this.getAssignedType()+"</assignedType>\n"+
                "<stepId>"+this.stepId+"</stepId>\n"+
                "<process>"+this.process+"</process>\n"+
                "<subBP>"+this.subBP+"</subBP>"+
                "<breakProcess>"+this.breakProcess+"</breakProcess>\n";
    }
}
