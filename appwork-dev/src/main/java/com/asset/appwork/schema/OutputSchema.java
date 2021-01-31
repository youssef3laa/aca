package com.asset.appwork.schema;

import com.asset.appwork.dto.Router;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class OutputSchema<T> {
    String taskId;
    String process;
    String stepId;
    Boolean breakProcess;
    String subBP;
    String entityName;
    String entityId;
    String page;
    String processId;
    String parentHistoryId;
    String decision;
    String comment;
    Boolean addApproval;
    String code;
    String assignedCN;
    String receiverType;
    HashMap<String, T> assignees = new HashMap<>();
    HashMap<String,T> extraData = new HashMap<>();
    Router router = new Router();

    public void setStepId(String stepId){
        this.breakProcess = stepId.equals("end");
        this.stepId = stepId;
    }

    public String getXML() throws AppworkException{
        return  "<assignedCN>"+removeNull(this.assignedCN)+"</assignedCN>"+
                "<stepId>"+removeNull(this.stepId)+"</stepId>"+
                "<process>"+removeNull(this.process)+"</process>"+
                "<subBP>"+removeNull(this.subBP)+"</subBP>"+
                "<entityName>"+removeNull(this.entityName)+"</entityName>"+
                "<entityId>"+removeNull(this.entityId)+"</entityId>"+
                "<parentHistoryId>"+removeNull(this.parentHistoryId)+"</parentHistoryId>"+
                "<page>"+removeNull(this.page)+"</page>"+
                "<decision>"+removeNull(this.decision)+"</decision>"+
                "<comment>"+removeNull(this.comment)+"</comment>"+
                "<breakProcess>"+removeNull(this.breakProcess)+"</breakProcess>"+
                "<addApproval>"+removeNull(this.addApproval)+"</addApproval>"+
                "<router>"+getObjectXML(this.router)+"</router>"+
                "<assignees>"+getObjectXML(this.assignees)+"</assignees>"+
                "<extraData>"+getObjectXML(this.extraData)+"</extraData>";
    }

    public String getXMLWithNameSpace() throws AppworkException{
        return "<ACA_ProcessRouting_OutputSchemaFragment xmlns=\"http://schemas.cordys.com/\">"+
                    getXML()+
                "</ACA_ProcessRouting_OutputSchemaFragment>";
    }

    private <T> String getObjectXML(T object) throws AppworkException{
        String xml = SystemUtil.convertObjectToXML(object);
        return xml;
    }

    public String getProcessFilePath(String basePath){
        return basePath + "\\" + this.process + ".json";
    }

    private String removeNull(Object object){
        if(object == null) return "";
        return object.toString();
    }
}
