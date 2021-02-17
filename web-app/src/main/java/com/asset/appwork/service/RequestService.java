package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.repository.RequestRepository;
import com.asset.appwork.schema.OutputSchema;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    OrgChartService orgChartService;

    public void updateRequest(OutputSchema outputSchema, String userCN, String entityId, String subject, String status) throws AppworkException {

        Optional<RequestEntity> request = requestRepository.findById(Long.parseLong(outputSchema.getRequestId()));
        if (request.isPresent()) {
            request.get().setDate(new Date());
            request.get().setInitiator(userCN);
            request.get().setEntityName(outputSchema.getEntityName());
            request.get().setEntityId(entityId);
            request.get().setProcess(outputSchema.getProcess());
            request.get().setSubject(subject);
            request.get().setStatus(status);

            requestRepository.save(request.get());
        } else {
            throw new AppworkException(ResponseCode.UPDATE_ENTITY_FAILURE);
        }
    }

    public String generateRequestNumber(Account account) throws AppworkException {
        try {
//            2020 create wared

            /*
            *
            * */

//            User user = orgChartService.getLoggedInUser(account);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.parse(sdf.format(date));
            long count = requestRepository.countByDate(Calendar.getInstance().get(Calendar.YEAR))+1;
            return count + "/" + sdf.format(date);
        } catch (ParseException e) {
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }


    public List<RequestEntity> getRequestsByProcessAndDateAndSubjectAndRequestNumber(@NonNull String process,
                                                                                     String subject,
                                                                                     String requestNumber) {
        return requestRepository.getRequestsByProcessAndDateAndSubjectAndRequestNumber(process, subject, requestNumber);
    }

    public RequestEntity getRequestEntityById(Long requestId) throws AppworkException {
        Optional<RequestEntity> optionalRequestEntity = requestRepository.findById(requestId);
        if (optionalRequestEntity.isEmpty()) throw new AppworkException(ResponseCode.NOT_EXIST);
        return optionalRequestEntity.get();
    }
}
