package com.rbi.loan.service;

import com.rbi.loan.model.RiskBand;
import com.rbi.loan.model.request.LoanApplicationRequest;
import com.rbi.loan.model.response.LoanResponse;
import com.rbi.loan.model.response.Offer;
import com.rbi.loan.repository.LoanApplicationEntity;
import com.rbi.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    private static final BigDecimal BASE_RATE = new BigDecimal("12.00");

    public LoanResponse processApplication(LoanApplicationRequest req) {
        List<String> rejections = new ArrayList<>();

        // 1. Eligibility Checks
        if (req.applicant().creditScore() < 600) rejections.add("CREDIT_SCORE_TOO_LOW");

        // Convert tenure to years for age calculation
        double tenureYears = req.loan().tenureMonths() / 12.0;
        if (req.applicant().age() + tenureYears > 65) rejections.add("AGE_TENURE_LIMIT_EXCEEDED");

        BigDecimal baseEmi = calculateEMI(req.loan().amount(), BASE_RATE, req.loan().tenureMonths());
        if (baseEmi.compareTo(req.applicant().monthlyIncome().multiply(new BigDecimal("0.60"))) > 0)
            rejections.add("EMI_EXCEEDS_60_PERCENT");

        if (!rejections.isEmpty()) return saveAndReturn(req, "REJECTED", null, null, rejections);

        // 2. Offer Generation using RiskBand Enum
        RiskBand riskBand = calculateRiskBand(req.applicant().creditScore());
        BigDecimal finalRate = calculateFinalRate(req, riskBand);
        BigDecimal finalEmi = calculateEMI(req.loan().amount(), finalRate, req.loan().tenureMonths());

        if (finalEmi.compareTo(req.applicant().monthlyIncome().multiply(new BigDecimal("0.50"))) > 0)
            return saveAndReturn(req, "REJECTED", riskBand, null, List.of("EMI_EXCEEDS_50_PERCENT"));

        BigDecimal totalPayable = finalEmi.multiply(BigDecimal.valueOf(req.loan().tenureMonths())).setScale(2, RoundingMode.HALF_UP);
        Offer offer = new Offer(finalRate, req.loan().tenureMonths(), finalEmi, totalPayable);

        return saveAndReturn(req, "APPROVED", riskBand, offer, null);
    }

    private BigDecimal calculateEMI(BigDecimal p, BigDecimal rAnn, int n) {
        BigDecimal r = rAnn.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        BigDecimal pow = r.add(BigDecimal.ONE).pow(n);
        return p.multiply(r).multiply(pow).divide(pow.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
    }

    private RiskBand calculateRiskBand(int score) {
        if (score >= 750) return RiskBand.LOW;
        if (score >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }

    private BigDecimal calculateFinalRate(LoanApplicationRequest req, RiskBand band) {
        BigDecimal rate = BASE_RATE;
        if (band == RiskBand.MEDIUM) rate = rate.add(new BigDecimal("1.5"));
        else if (band == RiskBand.HIGH) rate = rate.add(new BigDecimal("3.0"));

        if ("SELF_EMPLOYED".equals(req.applicant().employmentType())) rate = rate.add(BigDecimal.ONE);
        if (req.loan().amount().compareTo(new BigDecimal("1000000")) > 0) rate = rate.add(new BigDecimal("0.5"));
        return rate;
    }

    private LoanResponse saveAndReturn(LoanApplicationRequest req, String status, RiskBand band, Offer offer, List<String> reasons) {
        UUID appId = UUID.randomUUID();

        // Save to database
        LoanApplicationEntity entity = new LoanApplicationEntity();
        entity.setApplicationId(appId);
        entity.setApplicantName(req.applicant().name());
        entity.setStatus(status);
        entity.setRiskBand(band);
        loanRepository.save(entity);

        // If APPROVED, 'reasons' is null (so it disappears from JSON)
        // If REJECTED, 'offer' is null (so it disappears from JSON)
        return new LoanResponse(appId, status, band, offer, reasons);
    }
}
