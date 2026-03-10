package com.rbi.loan.model.request;

import java.math.BigDecimal;

public record Applicant(String name, int age, BigDecimal monthlyIncome, String employmentType, int creditScore) {
}
