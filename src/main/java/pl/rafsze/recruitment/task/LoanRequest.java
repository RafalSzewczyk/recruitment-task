package pl.rafsze.recruitment.task;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanRequest
{
	private int periodOfEmployment;
	private BigDecimal monthlyIncome;
	private BigDecimal monthlyLivingCosts;
	private BigDecimal monthlyLoanCosts;
	private BigDecimal totalLoanBalances;
	
}
