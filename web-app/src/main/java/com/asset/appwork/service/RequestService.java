package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.RequestRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    OrgChartService orgChartService;

    public String generateRequestNumber(Account account) throws AppworkException {
        try {
            User user = orgChartService.getLoggedInUser(account);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(sdf.format(date));
            long count = requestRepository.countDistinctByDateAfter(date) + 1;
            return sdf.format(date) + "-" + user.getId() + "-" + count;
        } catch (ParseException e) {
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        }
    }


    public List<RequestEntity> getRequestsByProcessAndDateAndSubjectAndRequestNumber(@NonNull String process,
                                                                                     @NonNull Date requestDate,
                                                                                     String subject,
                                                                                     String requestNumber) {

        return requestRepository.getRequestsByProcessAndDateAndSubjectAndRequestNumber(process, requestDate, subject, requestNumber);

    }
}
