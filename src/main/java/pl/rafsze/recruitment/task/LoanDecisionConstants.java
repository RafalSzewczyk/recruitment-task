package pl.rafsze.recruitment.task;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class LoanDecisionConstants
{
	private final int minLoanPeriod;
	private final int maxLoanPeriod;
	
	private final BigDecimal minLoanAmount;
	private final BigDecimal maxLoanAmount;
	
	private final BigDecimal maxTotalLoanAmount;
	
	private final BigDecimal dti;
	private final BigDecimal interest;
}

