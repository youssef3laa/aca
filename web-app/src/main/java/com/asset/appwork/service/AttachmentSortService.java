package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.AttachmentSort;
import com.asset.appwork.repository.AttachmentSortRepository;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentSortService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    AttachmentSortRepository attachmentSortRepository;

    public List<AttachmentSort> getAttachmentSort(String requestEntityId, String bwsId) {

        return attachmentSortRepository.getAllByBwsIdAndRequestEntityIdOrderByPositionAsc(bwsId, requestEntityId);
    }

    public AttachmentSort createAttachmentSort(AttachmentSort attachmentSort, Account account) throws AppworkException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.createObjectNode().set("Properties", mapper.valueToTree(attachmentSort));
            String dataJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            Http http = new Http().setDoAuthentication(true)
                    .setContentType(Http.ContentType.JSON_REQUEST)
                    .setData(dataJson)
                    .setHeader("SAMLArt", account.getSAMLart())
                    .post("http://appworks-aca:8080/home/aca/app/entityRestService/api/AssetGeneralACA/entities/ACA_Entity_attachmentSort");

            if (!http.isSuccess())
                throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));

            String id = mapper.readTree(http.getResponse()).get("Identity").get("Id").textValue();
            attachmentSort.setId(Long.valueOf(id));
            System.out.println(http.getResponse());
            return attachmentSort;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteAttachmentSort(Long id, Account account) throws AppworkException {
        Http http = new Http().setDoAuthentication(true)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("SAMLArt", account.getSAMLart())
                .delete("http://appworks-aca:8080/home/aca/app/entityRestService/api/AssetGeneralACA/entities/ACA_Entity_attachmentSort/items/" + id);
        System.out.println(account.getSAMLart());
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http.getResponse();
    }

    public AttachmentSort updateAttachmentSort(AttachmentSort attachmentSort) throws AppworkException {
        Optional<AttachmentSort> attachmentSortFoundOptional = attachmentSortRepository.findById(attachmentSort.getId());
        if (attachmentSortFoundOptional.isEmpty()) throw new AppworkException(ResponseCode.NOT_FOUND);
//        AttachmentSort attachmentSortFound = attachmentSortFoundOptional.get();
        return attachmentSortRepository.save(attachmentSort);
    }
}
