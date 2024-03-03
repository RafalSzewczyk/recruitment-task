package pl.rafsze.recruitment.task;

import java.math.BigDecimal;
import java.util.List;

import pl.rafsze.recruitment.task.PeriodDependentValue.PeriodDependentValueBuilder;

public class Main
{
	public static void main(String[] args)
	{
		PeriodDependentValue<LoanDecisionConstants> periodDependentConstants = prepareConstants();
		
		LoanOfferService loanOfferService = new SimpleLoanOfferService();
		
		LoanRequest request = new LoanRequest(150, new BigDecimal(6_000), new BigDecimal(2_000), new BigDecimal(1_000), new BigDecimal(50_000));
		
		List<LoanOffer> loanOffers = loanOfferService.getLoanOffers(request, periodDependentConstants);
		
		System.out.println("Liczba ofert: " + loanOffers.size());
		loanOffers.forEach(System.out::println);
	}
	
	private static PeriodDependentValue<LoanDecisionConstants> prepareConstants()
	{
		PeriodDependentValueBuilder<LoanDecisionConstants> builder = PeriodDependentValue.builder();
		
		builder.putValue(6, 12, 
				new LoanDecisionConstants(6, 12, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.6"), new BigDecimal("0.02")));
		

		builder.putValue(13, 36, 
				new LoanDecisionConstants(13, 36, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.6"), new BigDecimal("0.03")));
		

		builder.putValue(37, 60, 
				new LoanDecisionConstants(37, 60, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.5"), new BigDecimal("0.03")));
		

		builder.putValue(61, 100, 
				new LoanDecisionConstants(61, 100, new BigDecimal(5_000), new BigDecimal(150_000), new BigDecimal(200_000), new BigDecimal("0.55"), new BigDecimal("0.03")));
		
		return builder.build();
	}
}
