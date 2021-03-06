package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.SignatureDto;
import com.asset.appwork.dto.SignatureReadDto;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Signature;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.SignatureRepository;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class SignatureService {
    @Autowired
    Environment environment;

    @Autowired
    SignatureRepository signatureRepository;

    public void createSignature(Account account, SignatureDto signatureDto) throws AppworkException, IOException {

        long signaturesCount = signatureRepository.countAllByIncomingEntityId(signatureDto.getIncomingEntityId());

        @SuppressWarnings("StringBufferReplaceableByString") StringBuilder rootStringBuilder = new StringBuilder();

        rootStringBuilder.append(Objects.requireNonNull(environment.getProperty("signatures.dir")).replace("/", File.separator))
                .append(File.separator)
                .append(signatureDto.getIncomingEntityId())
                .append(File.separator)
                .append(new SimpleDateFormat("yyyy-MM-dd").format(signatureDto.getSignatureDate()))
                .append("-")
                .append(signatureDto.getViceOrHead())
                .append("-")
                .append(signaturesCount)
                .append(".png");
        String filePath = rootStringBuilder.toString();
        File file = new File(filePath);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (Files.exists(file.getParentFile().toPath())) {
            log.info("path: " + file.getParentFile().getAbsolutePath() + " is exists/created");
            log.info("mkdirs val is: " + mkdirs);
        }
        try (OutputStream outputStream = new FileOutputStream(file);
             InputStream inputStreamFile = new BufferedInputStream(signatureDto.getFile().getInputStream())) {
            byte[] buf = new byte[10000];
            int len;
            while ((len = inputStreamFile.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        }

        Entity entity = new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(environment, environment.getProperty("aca.general.solution")),
                Signature.TABLE);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Signature signature = modelMapper.map(signatureDto, Signature.class);
        if (signatureDto.getViceOrHead() == 1) {
            //for head
            signature.setSignatureHeadImgPath(filePath);
        } else if (signatureDto.getViceOrHead() == 2) {
            //for vice
            signature.setSignatureViceImgPath(filePath);
        }

        //create imgFile
        long signatureId = entity.create(signature);
        log.info("created signature with id: " + signatureId);
    }

    public List<Signature> getAllSignaturesByIncomingEntityId(long incomingEntityId) {
        return signatureRepository.findAllByIncomingEntityIdOrderBySignatureDateDescIdDesc(incomingEntityId);
    }


    public SignatureReadDto getSignatureData(long signatureEntityId) throws IOException {

        Optional<Signature> signatureOptional = signatureRepository.findById(signatureEntityId);
        if (signatureOptional.isEmpty()) {
            log.error("invalid signatureEntityId");
            return null;
        }

        Signature signature = signatureOptional.get();
        SignatureReadDto signatureReadDto = new SignatureReadDto();
        if (Objects.nonNull(signature.getSignatureHeadImgPath())) {
//            BufferedImage in = ImageIO.read(new FileInputStream(signature.getSignatureHeadImgPath()));
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ImageIO.write(SystemUtil.resizeImage(in, in.getWidth(), 600), "png", byteArrayOutputStream);
            signatureReadDto.setSignatureHeadImg(Files.readAllBytes(Paths.get(signature.getSignatureHeadImgPath())));
        }
        if (Objects.nonNull(signature.getSignatureViceImgPath())) {
            signatureReadDto.setSignatureViceImg(Files.readAllBytes(Paths.get(signature.getSignatureViceImgPath())));
        }
        signatureReadDto.setSignatureHeadTxt(signature.getSignatureHeadTxt());
        signatureReadDto.setSignatureViceTxt(signature.getSignatureViceTxt());

        return signatureReadDto;
    }

    public void updateSignatureTxt(long signatureEntityId, int viceOrHead, String text) {

    }
}
