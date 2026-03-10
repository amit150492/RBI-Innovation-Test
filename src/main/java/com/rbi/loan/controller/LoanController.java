package com.rbi.loan.controller;

import com.rbi.loan.model.request.LoanApplicationRequest;
import com.rbi.loan.model.response.LoanResponse;
import com.rbi.loan.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class LoanController {
    @Autowired
    private LoanService service;

    @PostMapping
    public ResponseEntity<LoanResponse> apply(@Valid @RequestBody LoanApplicationRequest req) {
        return ResponseEntity.ok(service.processApplication(req));
    }
}
