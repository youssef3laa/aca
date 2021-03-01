package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.model.Outcoming;
import com.asset.appwork.repository.LookupRepository;
import com.asset.appwork.repository.OutcomingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class OutcomingService {

    @Autowired
    OrgChartService orgChartService;
    @Autowired
    OutcomingRepository outcomingRepository;
    @Autowired
    LookupRepository lookupRepository;

    public String generateRequestNumber() throws AppworkException {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.parse(sdf.format(date));
            long count = outcomingRepository.countByOutcomingDate(Calendar.getInstance().get(Calendar.YEAR)) + 1;
            return count + "/" + sdf.format(date);
        } catch (ParseException e) {
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Outcoming findById(Long outcomingId) throws AppworkException{
        Optional<Outcoming> outcoming = outcomingRepository.findById(outcomingId);
        if(outcoming.isPresent()){
           return addLookupToOutcoming(outcoming.get());
        }else{
            throw new AppworkException(ResponseCode.NOT_FOUND);
        }
    }

    public Outcoming addLookupToOutcoming(Outcoming outcoming){
        Optional<Lookup> outcomingType = lookupRepository.findByCategoryAndKey("outcomingType", outcoming.getOutcomingType());
        if(outcomingType.isPresent()){
            outcoming.setOutcomingType(outcomingType.get().getArValue());
        }
        Optional<Lookup> outcomingIssueType = lookupRepository.findByCategoryAndKey("outcomingIssueType", outcoming.getOutcomingIssueType());
        if(outcomingIssueType.isPresent()){
            outcoming.setOutcomingIssueType(outcomingIssueType.get().getArValue());
        }

        return outcoming;
    }
}
