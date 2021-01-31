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
    String receiverType;
    Boolean addApproval;
    HashMap<String,T> extraData = new HashMap<>();
    List<String> assignees;
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
                "<router>"+createRouter()+"</router>"+
                "<extraData>"+createExtraData()+"</extraData>";
    }

    public String getXMLWithNameSpace() throws AppworkException{
        return "<ACA_ProcessRouting_OutputSchemaFragment xmlns=\"http://schemas.cordys.com/\">"+
                    getXML()+
                "</ACA_ProcessRouting_OutputSchemaFragment>";
    }

    private String createRouter() throws AppworkException{
        String router = SystemUtil.convertObjectToXML(this.router);
        return router;
    }

    private String createExtraData() throws AppworkException{
        String extraData = SystemUtil.convertObjectToXML(this.extraData);
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
