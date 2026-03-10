package com.rbi.loan.model.response;

import java.math.BigDecimal;

public record Offer(
        BigDecimal interestRate,
        int tenureMonths,
        BigDecimal emi,
        BigDecimal totalPayable
) {
}