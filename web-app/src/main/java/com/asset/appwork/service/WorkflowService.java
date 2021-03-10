package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.soap.Workflow;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;

@Slf4j
@Service
public class WorkflowService {
    @Autowired
    CordysService cordysService;

    @Autowired
    ApprovalHistoryService approvalHistoryService;

    public String claimTask(Account account, String taskId) throws JsonProcessingException, AppworkException {
        Workflow workflow = new Workflow();
        return cordysService.sendRequest(account, workflow.claimTask(account.getSAMLart(), taskId));
    }

    public String getTask(Account account, String taskId) throws IOException, AppworkException {
        Workflow workflow = new Workflow();
        String getTaskResponse = cordysService.sendRequest(account, workflow.getTask(account.getSAMLart(), taskId));
        log.info(getTaskResponse);

        Document document = SystemUtil.convertStringToXMLDocument(getTaskResponse);
        assert document != null;
        Node task = document.getElementsByTagName("Task").item(0);
        getTaskResponse = SystemUtil.convertDocumentNodetoJSON(task);


        return SystemUtil.readJSONField(getTaskResponse, "State");

    }
}
