package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.DelegationDTO;
import com.asset.appwork.dto.EscalationDTO;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Delegation;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.DelegationRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DelegationService {

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    OrgChartService orgChartService;

    @Autowired
    Environment env;

    public DelegationDTO createDelegation(Account account, String props) throws AppworkException, JsonProcessingException {
        return getDelegationDTO(
                new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, env.getProperty("aca.general.solution")), "ACA_Entity_Delegation")
                        .create(props)
        );
    }

    public Delegation getDelegation(Long id) throws AppworkException {
        return delegationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Delegation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public DelegationDTO getDelegationDTO(Long id) throws AppworkException {
        return delegationToDTO(getDelegation(id));
    }

    public Delegation updateDelegation(Long id, Delegation newDelegation) throws AppworkException, JsonProcessingException {
        Delegation delegation = getDelegation(id);
        delegation.setFrom(newDelegation.getFrom());
        delegation.setTo(newDelegation.getTo());
        delegation.setRole(newDelegation.getRole());
        delegation.setUserId(newDelegation.getUserId());
        delegation.setDelegatedTo(newDelegation.getDelegatedTo());
        delegation.setIsActive(newDelegation.getIsActive());
        delegationRepository.save(delegation);
        return getDelegation(id);
    }

    public void deleteDelegation(Long id) {
        delegationRepository.deleteById(id);
    }

    public List<DelegationDTO> getAllDelegation() {
        List<DelegationDTO> delegationDTOList = new ArrayList<>();
        delegationRepository.findAll().forEach(delegation -> {
            try {
                delegationDTOList.add(delegationToDTO(delegation));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return delegationDTOList;
    }

    public Page<DelegationDTO> getAllDelegation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Delegation> delegationPage = delegationRepository.findAll(pageable);
        List<DelegationDTO> delegationDTOList = new ArrayList<>();

        delegationPage.getContent().forEach(delegation -> {
            try {
                delegationDTOList.add(delegationToDTO(delegation));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return new PageImpl<>(delegationDTOList, pageable, delegationPage.getTotalElements());
    }

    public List<DelegationDTO> getAllDelegationSearchable(String search) {
        List<DelegationDTO> delegationDTOList = new ArrayList<>();
        delegationRepository.findAll().forEach(delegation -> {
            try {
                delegationDTOList.add(delegationToDTO(delegation));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return searchInDelegationDTO(delegationDTOList, search);
    }

    public Page<DelegationDTO> getAllDelegationSearchable(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Delegation> delegationPage = delegationRepository.findAll(pageable);
        List<DelegationDTO> delegationDTOList = new ArrayList<>();

        delegationPage.getContent().forEach(delegation -> {
            try {
                delegationDTOList.add(delegationToDTO(delegation));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return new PageImpl<>(
                searchInDelegationDTO(delegationDTOList, search),
                pageable, delegationPage.getTotalElements());
    }

    private DelegationDTO delegationToDTO(Delegation delegation) throws AppworkException {
        DelegationDTO delegationDTO = new DelegationDTO();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        delegationDTO.setId(delegation.getId());;
        delegationDTO.setFrom(dateFormat.format(delegation.getFrom()));
        delegationDTO.setTo(dateFormat.format(delegation.getTo()));
        delegationDTO.setGroup(orgChartService.getGroupByName(delegation.getRole()));
        delegationDTO.setUser(orgChartService.getUserByUserId(delegation.getUserId()));
        delegationDTO.setDelegatedGroup(orgChartService.getGroupByName(delegation.getDelegatedTo()));
        delegationDTO.setIsActive(delegation.getIsActive());

        return delegationDTO;
    }

    private List<DelegationDTO> searchInDelegationDTO(List<DelegationDTO> delegationDTOItems, String search) {
        return
                delegationDTOItems.stream().filter(
                        delegationDTO -> (
                                delegationDTO.getFrom().contains(search) ||
                                delegationDTO.getTo().contains(search) ||
                                delegationDTO.getGroup().getNameAr().contains(search) ||
                                delegationDTO.getUser().getDisplayName().contains(search) ||
                                delegationDTO.getDelegatedGroup().getNameAr().contains(search)
                        )
                ).collect(Collectors.toList());
    }
}