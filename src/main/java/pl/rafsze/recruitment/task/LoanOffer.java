package pl.rafsze.recruitment.task;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LoanOffer
{
	private int maxLoanPeriod;
	private BigDecimal maxLoanInstallment;
	private BigDecimal maxLoanAmount;
}
