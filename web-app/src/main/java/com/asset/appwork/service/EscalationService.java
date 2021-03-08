package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.EscalationDTO;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Escalation;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.EscalationRepository;
import com.asset.appwork.repository.LookupRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EscalationService {

    @Autowired
    EscalationRepository escalationRepository;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    Environment env;

    public EscalationDTO createEscalation(Account account, String props) throws AppworkException, JsonProcessingException {
        return getEscalationDTO(
                new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, env.getProperty("aca.general.solution")), "ACA_Entity_Escalation")
                        .create(props)
        );
    }

    public Escalation getEscalation(Long id) throws AppworkException {
        return escalationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Escalation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public EscalationDTO getEscalationDTO(Long id) throws AppworkException {
        return escalationToDTO(escalationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Escalation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        ));
    }

    public EscalationDTO updateEscalation(Long id, Escalation newEscalation) throws AppworkException, JsonProcessingException {
        Escalation escalation = getEscalation(id);
        escalation.setDuration(newEscalation.getDuration());
        escalation.setExtension(newEscalation.getExtension());
        escalation.setJobType(newEscalation.getJobType());
        escalation.setUnitType(newEscalation.getUnitType());
        escalationRepository.save(escalation);
        return getEscalationDTO(id);
    }

    public void deleteEscalation(Long id) {
        escalationRepository.deleteById(id);
    }

    public List<EscalationDTO> getAllEscalation() throws AppworkException {
        List<EscalationDTO> escalationDTOList = new ArrayList<>();
        escalationRepository.findAll().forEach(escalation -> {
            try {
                escalationDTOList.add(escalationToDTO(escalation));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return escalationDTOList;
    }

    public List<EscalationDTO> getAllEscalationJobTypes(String search) throws AppworkException {
        List<EscalationDTO> escalationDTOList = new ArrayList<>();
        Page<Lookup> jobTypes = lookupRepository.findCategoryValues("jobType", "", PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id")));
        jobTypes.forEach(jobType -> {
            try {
                escalationDTOList.add(JobTypeToEscalationDTO(jobType));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return searchInEscalationDTO(escalationDTOList, search);
    }

    public Page<EscalationDTO> getAllEscalationJobTypes(String search, int page, int size) throws AppworkException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Lookup> lookupPage = lookupRepository.findCategoryValues("jobType", "", pageable);
        List<EscalationDTO> escalationDTOList = new ArrayList<>();

        lookupPage.getContent().forEach(jobType -> {
            try {
                escalationDTOList.add(JobTypeToEscalationDTO(jobType));
            } catch (AppworkException e) {
                e.printStackTrace();
            }
        });

        return new PageImpl<>(searchInEscalationDTO(
                escalationDTOList,
                search), pageable, lookupPage.getTotalElements());
    }

    private Lookup getJobTitleByKey(Integer key) throws AppworkException {
        return lookupRepository.findByCategoryAndKey("jobType", key.toString()).orElseThrow(
                () -> new AppworkException("Could not get Lookup Entity of category: jobType and key: " + key, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    private Lookup getUnitTypeByKey(Integer key) throws AppworkException {
        return lookupRepository.findByCategoryAndKey("unitType", key.toString()).orElseThrow(
                () -> new AppworkException("Could not get Lookup Entity of category: unitType and key: " + key, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    private EscalationDTO escalationToDTO(Escalation escalation) throws AppworkException {
        EscalationDTO escalationDTO = new EscalationDTO();
        escalationDTO.setId(escalation.getId());
        escalationDTO.setDuration(escalation.getDuration());
        escalationDTO.setExtension(escalation.getExtension());
        escalationDTO.setJobType(getJobTitleByKey(escalation.getJobType()));
        escalationDTO.setUnitType(getUnitTypeByKey(escalation.getUnitType()));
        return escalationDTO;
    }

    private EscalationDTO JobTypeToEscalationDTO(Lookup jobType) throws AppworkException {
        AtomicReference<EscalationDTO> escalationDTO = new AtomicReference<>();
        Optional<Escalation> escalation = escalationRepository.findByJobType(Integer.parseInt(jobType.getKey()));

        escalation.ifPresentOrElse(esc -> {
            try {
                escalationDTO.set(escalationToDTO(esc));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }, () -> {
            EscalationDTO escDTO = new EscalationDTO();
            escDTO.setDuration(0);
            escDTO.setExtension(0);
            escDTO.setJobType(jobType);

            escalationDTO.set(escDTO);
        });
        return escalationDTO.get();
    }

    private List<EscalationDTO> searchInEscalationDTO(List<EscalationDTO> escalationDTOItems, String search) {
        return
                escalationDTOItems.stream().filter(
                        escalationDTO -> (
                                escalationDTO.getDuration().toString().contains(search) ||
                                escalationDTO.getExtension().toString().contains(search) ||
                                (
//                                        escalationDTO.getJobType() != null &&
                                        escalationDTO.getJobType().getArValue().contains(search)
                                ) ||
                                (
                                    escalationDTO.getUnitType() != null &&
                                    escalationDTO.getUnitType().getArValue().contains(search)
                                )
                        )
                ).collect(Collectors.toList());
    }
}
