package com.rbi.loan.model.request;

import java.math.BigDecimal;

public record LoanDetails(BigDecimal amount, int tenureMonths, String purpose) {
}
