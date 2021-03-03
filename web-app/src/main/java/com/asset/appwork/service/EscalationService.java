package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Escalation;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.EscalationRepository;
import com.asset.appwork.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EscalationService {

    @Autowired
    EscalationRepository escalationRepository;

    @Autowired
    Environment env;

    public Escalation createEscalation(Account account, String props) throws AppworkException {
        return getEscalation(new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Escalation")
                .create(props));
    }

    public Escalation getEscalation(Long id) throws AppworkException {
        return escalationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Escalation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Escalation updateEscalation(Account account, Long id, String props) throws AppworkException {
        Escalation escalation = getEscalation(id);
        Escalation newEscalation = Escalation.fromString(props);
        escalation.setDuration(newEscalation.getDuration());
//        escalation.setJobType();
        return escalationRepository.save(escalation);
    }

    public void deleteEscalation(Account account, Long id) {
        escalationRepository.deleteById(id);
    }
}
