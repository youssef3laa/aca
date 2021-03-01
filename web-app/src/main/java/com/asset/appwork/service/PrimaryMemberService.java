package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.PrimaryMemberDto;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.PrimaryMember;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.PrimaryMemberRepository;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PrimaryMemberService {


    @Autowired
    PrimaryMemberRepository primaryMemberRepository;
    @Autowired
    Environment environment;

    public PrimaryMemberDto createPrimaryMember(Account account, PrimaryMemberDto primaryMemberDto) throws AppworkException {
        Optional<PrimaryMember> optionalPrimaryMember = primaryMemberRepository.findByUserCNAndIncomingRegistration_id(primaryMemberDto.getUserCN(),
                primaryMemberDto.getIncomingRegistrationEntityId());

        if (optionalPrimaryMember.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            PrimaryMember member = optionalPrimaryMember.get();
            PrimaryMemberDto primaryMemberDtoRes = modelMapper.map(member, PrimaryMemberDto.class);
            primaryMemberDtoRes.setIncomingRegistrationEntityId(primaryMemberDto.getIncomingRegistrationEntityId());
            return primaryMemberDtoRes;
        }
        Entity entity = new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(environment, environment.getProperty("aca.general.solution")),
                PrimaryMember.TableName);
        primaryMemberDto.setId(entity.create(primaryMemberDto));
        return primaryMemberDto;
    }

    public PrimaryMemberDto findFirstPrimaryMember(PrimaryMemberDto primaryMemberDto) throws AppworkException {
        Optional<PrimaryMember> optionalPrimaryMember = primaryMemberRepository.findTopByIncomingRegistration_idOrderByIdDesc(primaryMemberDto.getIncomingRegistrationEntityId());

        if (optionalPrimaryMember.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            PrimaryMember member = optionalPrimaryMember.get();
            PrimaryMemberDto primaryMemberDtoRes = modelMapper.map(member, PrimaryMemberDto.class);
            primaryMemberDtoRes.setIncomingRegistrationEntityId(primaryMemberDto.getIncomingRegistrationEntityId());
            return primaryMemberDtoRes;
        }
        throw new AppworkException(ResponseCode.NOT_EXIST);
    }
}
