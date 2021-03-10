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

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/audit")

@RestController
public class AuditController {
    @Autowired
    AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createAudit(@RequestHeader("X-Auth-Token") String token, @RequestBody Audit audit) {
        return auditService.createAudit(token, audit);
    }

    @GetMapping()
    public ResponseEntity<AppResponse<List<Audit>>> getAllAudit(@RequestHeader("X-Auth-Token") String token,
                                                                @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                                @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "200") int size) {

        if(search.isEmpty()) return auditService.getAllAudit(token, page, size);
        return auditService.getAuditBySearch(token,search, page, size);

    }
    
}
