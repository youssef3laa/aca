package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Audit;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.AuditService;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/audit")

@RestController
public class AuditController {
    @Autowired
    AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createAudit(@RequestHeader("X-Auth-Token") String token, @RequestBody Audit audit){

        return auditService.createAudit(token,audit);
    }
}
