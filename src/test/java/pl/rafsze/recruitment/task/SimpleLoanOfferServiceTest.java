package pl.rafsze.recruitment.task;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import pl.rafsze.recruitment.task.PeriodDependentValue.PeriodDependentValueBuilder;

public class SimpleLoanOfferServiceTest
{
	private final LoanOfferService loanOfferService = new SimpleLoanOfferService();
	
	@Test
	public void testPositiveVerification()
	{
		PeriodDependentValueBuilder<LoanDecisionConstants> builder = PeriodDependentValue.builder();
		
		builder.putValue(6, 12, 
				new LoanDecisionConstants(6, 12, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.6"), new BigDecimal("0.02")));
		PeriodDependentValue<LoanDecisionConstants> parameters = builder.build();
		
		LoanRequest request = new LoanRequest(36, new BigDecimal(10_000), new BigDecimal(1_000), BigDecimal.ZERO, BigDecimal.ZERO);
		
		List<LoanOffer> loanOffers = loanOfferService.getLoanOffers(request, parameters);
		assertEquals(1, loanOffers.size());
	}
	
	@Test
	public void testPeriodOfEmploymentLessThanMinLoanPeriod()
	{
		PeriodDependentValueBuilder<LoanDecisionConstants> builder = PeriodDependentValue.builder();
		
		builder.putValue(6, 12, 
				new LoanDecisionConstants(6, 12, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.6"), new BigDecimal("0.02")));
		PeriodDependentValue<LoanDecisionConstants> parameters = builder.build();
		
		LoanRequest request = new LoanRequest(3, new BigDecimal(10_000), new BigDecimal(1_000), BigDecimal.ZERO, BigDecimal.ZERO);
		
		List<LoanOffer> loanOffers = loanOfferService.getLoanOffers(request, parameters);
		assertEquals(0, loanOffers.size());
	}
	
	@Test
	public void testTotalLoanAmountGreaterThanAllowed()
	{
		PeriodDependentValueBuilder<LoanDecisionConstants> builder = PeriodDependentValue.builder();
		
		builder.putValue(6, 12, 
				new LoanDecisionConstants(6, 12, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.6"), new BigDecimal("0.02")));
		PeriodDependentValue<LoanDecisionConstants> parameters = builder.build();
		
		LoanRequest request = new LoanRequest(24, new BigDecimal(10_000), new BigDecimal(1_000), BigDecimal.ZERO, new BigDecimal(300_000));
		
		List<LoanOffer> loanOffers = loanOfferService.getLoanOffers(request, parameters);
		assertEquals(0, loanOffers.size());
	}
	
	@Test
	public void testMaxAvailableLoanAmountLessThanMinLoanAmount()
	{
		PeriodDependentValueBuilder<LoanDecisionConstants> builder = PeriodDependentValue.builder();
		
		builder.putValue(6, 12, 
				new LoanDecisionConstants(6, 12, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.6"), new BigDecimal("0.02")));
		PeriodDependentValue<LoanDecisionConstants> parameters = builder.build();
		
		LoanRequest request = new LoanRequest(24, new BigDecimal(10_000), new BigDecimal(1_000), BigDecimal.ZERO, new BigDecimal(197_000));
		
		List<LoanOffer> loanOffers = loanOfferService.getLoanOffers(request, parameters);
		assertEquals(0, loanOffers.size());
	}
	
}
