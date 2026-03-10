package com.rbi.loan.repository;

import com.rbi.loan.model.RiskBand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_applications")
@Getter
@Setter
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
