package com.asset.appwork.schema;

import com.asset.appwork.dto.Assignees;
import com.asset.appwork.dto.Router;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class OutputSchema<T> {
    Boolean breakProcess;
    Boolean addApproval;
    String taskId;
    String subBP;
    String parentHistoryId;
    String process, processId, stepId;
    String requestId, entityName;
    String component, config, readonlyComponent;
    String decision, comment, opinion;
    String code, assignedCN;
    String receiverType;
    Router router = new Router();
    Assignees assignees = new Assignees();
    HashMap<String,T> extraData = new HashMap<>();

    public void setStepId(String stepId){
        this.breakProcess = stepId.equals("end");
        this.stepId = stepId;
    }

    public String getXML() throws AppworkException{
        return  "<assignedCN>"+removeNull(this.assignedCN)+"</assignedCN>"+
                "<stepId>"+removeNull(this.stepId)+"</stepId>"+
                "<process>"+removeNull(this.process)+"</process>"+
                "<subBP>"+removeNull(this.subBP)+"</subBP>"+
                "<requestId>"+removeNull(this.requestId)+"</requestId>"+
                "<parentHistoryId>"+removeNull(this.parentHistoryId)+"</parentHistoryId>"+
                "<component>"+removeNull(this.component)+"</component>"+
                "<config>"+removeNull(this.config)+"</config>"+
                "<readonlyComponent>"+removeNull(this.readonlyComponent)+"</readonlyComponent>"+
                "<decision>"+removeNull(this.decision)+"</decision>"+
                "<comment>"+removeNull(this.comment)+"</comment>"+
                "<opinion>"+removeNull(this.opinion)+"</opinion>"+
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
