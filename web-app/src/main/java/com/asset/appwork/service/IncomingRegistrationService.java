package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IncomingRegistration;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.repository.IncomingRegistrationRepository;
import com.asset.appwork.repository.LookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IncomingRegistrationService {

    @Autowired
    LookupRepository lookupRepository;
    @Autowired
    IncomingRegistrationRepository incomingRegistrationRepository;

    @Autowired
    OrgChartService orgChartService;

    public void updateWithOutcomingId(Long registrationId, Long outcomingId) throws AppworkException {
        Optional<IncomingRegistration> incomingRegistration = incomingRegistrationRepository.findById(registrationId);
        if (incomingRegistration.isPresent()) {
            incomingRegistration.get().setOutcomingId(outcomingId);
            incomingRegistrationRepository.save(incomingRegistration.get());
        } else {
            throw new AppworkException(ResponseCode.NO_CONTENT);
        }
    }

    public IncomingRegistration findById(Long id) throws AppworkException {
        Optional<IncomingRegistration> incomingRegistration = incomingRegistrationRepository.findById(id);
        if (incomingRegistration.isPresent()) {
            return addLookupToIncomingRegistration(incomingRegistration.get());
        } else {
            throw new AppworkException(ResponseCode.NO_CONTENT);
        }
    }

    public IncomingRegistration addLookupToIncomingRegistration(IncomingRegistration incomingRegistration) {
        Optional<Lookup> confidentiality = lookupRepository.findByCategoryAndKey("confidentialityType", incomingRegistration.getConfidentiality());
        confidentiality.ifPresent(lookup -> incomingRegistration.setConfidentialityTxt(lookup.getArValue()));

        Optional<Lookup> incomingType = lookupRepository.findByCategoryAndKey("incomingType", incomingRegistration.getIncomingType());
        incomingType.ifPresent(lookup -> incomingRegistration.setIncomingTypeTxt(lookup.getArValue()));

        Optional<Lookup> jobType = lookupRepository.findByCategoryAndKey("jobType", incomingRegistration.getJobType());
        jobType.ifPresent(lookup -> incomingRegistration.setJobTypeTxt(lookup.getArValue()));

        Optional<Lookup> priorityLevel = lookupRepository.findByCategoryAndKey("priority", incomingRegistration.getPriorityLevel());
        priorityLevel.ifPresent(lookup -> incomingRegistration.setPriorityLevelTxt(lookup.getArValue()));

        Optional<Lookup> taskType = lookupRepository.findByCategoryAndKey("taskType", incomingRegistration.getTaskType());
        taskType.ifPresent(lookup -> incomingRegistration.setTaskTypeTxt(lookup.getArValue()));
        Optional<Lookup> incomingFrom = lookupRepository.findByCategoryAndKey("incomingSources", incomingRegistration.getIncomingFrom());
        incomingFrom.ifPresent(lookup -> incomingRegistration.setIncomingFromTxt(lookup.getArValue()));

        Optional<String> responsibleEntityGehaz = Optional.ofNullable(incomingRegistration.getResponsibleEntityGehaz());
        responsibleEntityGehaz.ifPresent(element -> {
            try {
                incomingRegistration.setResponsibleEntityGehazTxt(orgChartService.getUnitByName(element).getNameAr());
            } catch (AppworkException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }

        });
        Optional<String> responsibleEntityKeta3 = Optional.ofNullable(incomingRegistration.getResponsibleEntityketa3());
        responsibleEntityKeta3.ifPresent(element -> {
            try {
                incomingRegistration.setResponsibleEntityKeta3Txt(orgChartService.getUnitByName(element).getNameAr());
            } catch (AppworkException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        });

        Optional<String> responsibleEntityEdara = Optional.ofNullable(incomingRegistration.getResponsibleEntityEdara());
        responsibleEntityEdara.ifPresent(element -> {
            try {
                incomingRegistration.setResponsibleEntityEdaraTxt(orgChartService.getUnitByName(element).getNameAr());
            } catch (AppworkException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        });


        return incomingRegistration;
    }

    public IncomingRegistration updateIncomingEntity(IncomingRegistration incomingRegistration) {
        return incomingRegistrationRepository.save(incomingRegistration);
    }
}
