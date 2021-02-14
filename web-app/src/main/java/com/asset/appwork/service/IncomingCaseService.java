package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IncomingCase;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.repository.IncomingCaseRepository;
import com.asset.appwork.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IncomingCaseService {

    @Autowired
    LookupRepository lookupRepository;
    @Autowired
    IncomingCaseRepository incomingCaseRepository;

    public IncomingCase findById(Long id) throws AppworkException {
        Optional<IncomingCase> incomingCase = incomingCaseRepository.findById(id);
        if(incomingCase.isPresent()){
            return addLookupToIncomingCase(incomingCase.get());
        }else{
            throw new AppworkException(ResponseCode.NO_CONTENT);
        }
    }

    public IncomingCase addLookupToIncomingCase(IncomingCase incomingCase){
        Optional<Lookup> caseType = lookupRepository.findByCategoryAndKey("caseType",incomingCase.getCaseType());
        if(caseType.isPresent()){
            incomingCase.setCaseType(caseType.get().getArValue());
        }

        return incomingCase;
    }
}
