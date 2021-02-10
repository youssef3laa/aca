package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "AssetGeneralACAACA_Entity_attachmentSort")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AttachmentSort {
    @Id
    private Long id;
    @NonNull
    private String bwsId;
    @NonNull
    private String fileId;
    private String position;
    @NonNull
    private String requestEntityId;

}
