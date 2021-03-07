package com.asset.appwork.dto;

import lombok.Data;

@Data
public class SignatureReadDto {

    private byte[] signatureHeadImg;
    private byte[] signatureViceImg;
    private String signatureHeadTxt;
    private String signatureViceTxt;
}
