package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ProcessTempSave;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.soup.ProcessSOAP;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class ProcessService {

    @Autowired
    CordysService cordysService;
    @Autowired
    Environment environment;
    @Autowired
    ModuleRouting moduleRouting;
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    OrgChartService orgChartService;
    @Autowired
    RequestService requestService;

    public void pauseProcess(Account account, OutputSchema outputSchema) throws AppworkException, IOException, ParseException {
        RequestEntity requestEntity = requestService.getRequestEntityById(Long.valueOf(outputSchema.getRequestId()));

        //calc outputSchema
//        String cordysUrl = cordysService.getCordysUrl();

//        String filePath = outputSchema.getProcessFilePath(environment.getProperty("process.config"));
//        String config = SystemUtil.readFile(filePath);

//        ModuleRouting moduleRouting = new ModuleRouting(account, cordysUrl, config, approvalHistoryRepository);

        // create entity
        Entity processTempSaveEntity =
                new Entity(account, SystemUtil.generateRestAPIBaseUrl(environment, environment.getProperty("aca.general.solution")), "ACA_Entity_processTempSave");
        ProcessTempSave processTempSave = new ProcessTempSave();
        processTempSave.setProcessName(requestEntity.getProcess());
        processTempSave.setTaskId(outputSchema.getTaskId());
        processTempSave.setPauseDate(new SimpleDateFormat("yyyy-MM-dd").parse((String) outputSchema.getExtraData().get("pauseDate")));
        processTempSave.setResumeDate(new SimpleDateFormat("yyyy-MM-dd").parse((String) outputSchema.getExtraData().get("resumeDate")));
        processTempSave.setOutputSchema(moduleRouting.calculateOutputSchema(outputSchema, account));
        processTempSave.setProcessInstanceId(requestEntity.getProcessInstanceId());
        processTempSave.setStatus("paused");
        processTempSaveEntity.create(processTempSave);

        pauseProcessInAppwork(account, requestEntity.getProcessInstanceId());
    }

    public void pauseProcessInAppwork(Account account, String processInstanceId) throws JsonProcessingException, AppworkException {
        String pauseProcessResponse = cordysService.sendRequest(account, new ProcessSOAP().pauseProcessMsg(processInstanceId));
        log.info("sent pause process to process instance Id: " + processInstanceId + "\nand the response is :\n" + pauseProcessResponse);
    }
}
