package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Inquiry;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.InquiryRepository;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IncomingInquiryService {


    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    Environment environment;

    public Inquiry createInquiry(Account account, Inquiry inquiry) throws AppworkException {
        Entity entity =
                new Entity(account,
                        SystemUtil.generateRestAPIBaseUrl(environment, environment.getProperty("aca.general.solution")),
                        "ACA_Entity_inquiry");
        inquiry.setId(entity.create(inquiry));
        return inquiry;
    }

    public Page<Inquiry> getAllInquiriesByIncomingId(Long incomingEntityId, Optional<Integer> page, Optional<Integer> size, Optional<String> search) throws AppworkException {
//        IncomingRegistration incomingRegistration = new IncomingRegistration();
//        incomingRegistration.setId(incomingEntityId);

        return inquiryRepository.getInquiriesByIncomingRegistrationId(incomingEntityId, search.get(), PageRequest.of(page.get(), size.get(), Sort.by("id")));
    }

    public void deleteInquiry(Account account, Long inquiryEntityId) throws AppworkException {
        Entity entity =
                new Entity(account,
                        SystemUtil.generateRestAPIBaseUrl(environment, environment.getProperty("aca.general.solution")),
                        "ACA_Entity_inquiry");
        String deleteResponse = entity.delete(inquiryEntityId);
        log.info("inquiry entity is deleted and the response is\n" + deleteResponse);

    }


    public Inquiry updateInquiry(Inquiry inquiry) throws AppworkException {
        if (!inquiryRepository.existsById(inquiry.getId())) throw new AppworkException(ResponseCode.NOT_FOUND);
        return inquiryRepository.save(inquiry);
    }

}
