package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IncomingCase;
import com.asset.appwork.model.IncomingRegistration;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.repository.IncomingRegistrationRepository;
import com.asset.appwork.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IncomingRegistrationService {

    @Autowired
    LookupRepository lookupRepository;
    @Autowired
    IncomingRegistrationRepository incomingRegistrationRepository;

    public void updateWithOutcomingId(Long registrationId, Long outcomingId) throws AppworkException{
        Optional<IncomingRegistration> incomingRegistration = incomingRegistrationRepository.findById(registrationId);
        if(incomingRegistration.isPresent()){
            incomingRegistration.get().setOutcomingId(outcomingId);
            incomingRegistrationRepository.save(incomingRegistration.get());
        }else{
            throw new AppworkException(ResponseCode.NO_CONTENT);
        }
    }

    public IncomingRegistration findById(Long id) throws AppworkException{
        Optional<IncomingRegistration> incomingRegistration = incomingRegistrationRepository.findById(id);
        if(incomingRegistration.isPresent()){
            return addLookupToIncomingRegistration(incomingRegistration.get());
        }else{
            throw new AppworkException(ResponseCode.NO_CONTENT);
        }
    }

    public IncomingRegistration addLookupToIncomingRegistration(IncomingRegistration incomingRegistration){
        Optional<Lookup> confidentiality = lookupRepository.findByCategoryAndKey("confidentialityType", incomingRegistration.getConfidentiality());
        if(confidentiality.isPresent()){
            incomingRegistration.setConfidentiality(confidentiality.get().getArValue());
        }
        Optional<Lookup> incomingType = lookupRepository.findByCategoryAndKey("incomingType", incomingRegistration.getIncomingType());
        if(incomingType.isPresent()){
            incomingRegistration.setIncomingType(incomingType.get().getArValue());
        }
        Optional<Lookup> jobType = lookupRepository.findByCategoryAndKey("jobType", incomingRegistration.getJobType());
        if(jobType.isPresent()){
            incomingRegistration.setJobType(jobType.get().getArValue());
        }
        Optional<Lookup> priorityLevel = lookupRepository.findByCategoryAndKey("priority", incomingRegistration.getPriorityLevel());
        if(priorityLevel.isPresent()){
            incomingRegistration.setPriorityLevel(priorityLevel.get().getArValue());
        }
        Optional<Lookup> taskType = lookupRepository.findByCategoryAndKey("taskType", incomingRegistration.getTaskType());
        if(taskType.isPresent()){
            incomingRegistration.setTaskType(taskType.get().getArValue());
        }

        return incomingRegistration;
    }
}
