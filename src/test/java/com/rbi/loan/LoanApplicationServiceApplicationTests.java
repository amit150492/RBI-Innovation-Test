package com.rbi.loan;

import com.rbi.loan.model.request.Applicant;
import com.rbi.loan.model.request.LoanApplicationRequest;
import com.rbi.loan.model.request.LoanDetails;
import com.rbi.loan.model.response.LoanResponse;
import com.rbi.loan.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanApplicationServiceApplicationTests {

    private final LoanService loanService = new LoanService();

    @Test
    void testCalculateEMI_CorrectValue() {
        // Principal 500,000, 12% annual rate, 36 months
        BigDecimal emi = loanService.calculateEMI(new BigDecimal("500000"), new BigDecimal("12.00"), 36);
        // Expected value based on your calculation formula
        assertEquals(new BigDecimal("16607.15"), emi);
    }

    @Test
    void testProcessApplication_Approved() {
        // Arrange
        Applicant applicant = new Applicant("Amit", 30, new BigDecimal("75000"), "SALARIED", 750);
        LoanDetails loan = new LoanDetails(new BigDecimal("500000"), 36, "PERSONAL");
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, loan);

        // Act
        LoanResponse response = loanService.processApplication(request);

        // Assert
        assertEquals("APPROVED", response.status());
        assertNotNull(response.offer());
    }

    @Test
    void testProcessApplication_Rejected_CreditScoreTooLow() {
        // Arrange: Credit score 500 (below 600 requirement)
        Applicant applicant = new Applicant("Rahul", 30, new BigDecimal("75000"), "SALARIED", 500);
        LoanDetails loan = new LoanDetails(new BigDecimal("500000"), 36, "PERSONAL");
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, loan);

        // Act
        LoanResponse response = loanService.processApplication(request);

        // Assert
        assertEquals("REJECTED", response.status());
        assertTrue(response.rejectionReasons().contains("CREDIT_SCORE_TOO_LOW"));
    }

}
