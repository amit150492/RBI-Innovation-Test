package com.rbi.loan.repository;

import com.rbi.loan.model.RiskBand;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class LoanApplicationEntity {
    @Id
    private UUID applicationId;
    private String applicantName;
    private int applicantAge;
    private BigDecimal monthlyIncome;
    private int creditScore;

    @Enumerated(EnumType.STRING)
    private RiskBand riskBand;

    private String status; // APPROVED or REJECTED

    private BigDecimal loanAmount;
    private int tenureMonths;

    private BigDecimal interestRate;
    private BigDecimal emi;

    @Column(length = 500)
    private String rejectionReasons; // Stored as comma-separated string

    private LocalDateTime createdAt;
}
