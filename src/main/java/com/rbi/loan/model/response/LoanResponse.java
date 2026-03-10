package com.rbi.loan.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbi.loan.model.RiskBand;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoanResponse(
        UUID applicationId,
        String status,
        RiskBand riskBand,
        Offer offer,
        List<String> rejectionReasons
) {
}