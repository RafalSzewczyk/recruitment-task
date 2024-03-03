package pl.rafsze.recruitment.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import lombok.Data;

public class SimpleLoanOfferService implements LoanOfferService
{

	@Override
	public List<LoanOffer> getLoanOffers(LoanRequest request, PeriodDependentValue<LoanDecisionConstants> parameters)
	{
		validateRequest(request);
		
		return parameters.getValues().stream()
				.map(constants -> getOfferForPeriod(request, constants))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	private void validateRequest(LoanRequest request)
	{
		Validate.isTrue(request.getPeriodOfEmployment() > 0, "periodOfEmployment must be greater than 0");
		Validate.isTrue(request.getMonthlyIncome().signum() >= 0, "monthlyIncome cannot be negative");
		Validate.isTrue(request.getMonthlyLivingCosts().signum() >= 0, "monthlyLivingCosts cannot be negative");
		Validate.isTrue(request.getMonthlyLoanCosts().signum() >= 0, "monthlyLoanCosts cannot be negative");
		Validate.isTrue(request.getTotalLoanBalances().signum() >= 0, "totalLoanBalances cannot be negative");
	}
	
	private LoanOffer getOfferForPeriod(LoanRequest request, LoanDecisionConstants constants)
	{
		LoanOffer offer = new LoanOffer();
		LoanDecisionContext context = new LoanDecisionContext(request, offer, constants);
		
		calculateMaxAvailableLoanPeriod(context);
		if ( offer.getMaxLoanPeriod() < constants.getMinLoanPeriod() )
		{
			return null;
		}
		
		calculateMaxLoanInstallment(context);
		if ( offer.getMaxLoanInstallment().signum() <= 0 )
		{
			return null;
		}
		
		calculateMaxLoanAmount(context);
		if ( offer.getMaxLoanAmount().compareTo(constants.getMinLoanAmount()) < 0 )
		{
			return null;
		}
		
		return offer;
	}
	
	private void calculateMaxAvailableLoanPeriod(LoanDecisionContext context)
	{
		int maxAvailableLoanPeriod = Math.min(context.getRequest().getPeriodOfEmployment(), context.getConstants().getMaxLoanPeriod());
		context.getOffer().setMaxLoanPeriod(maxAvailableLoanPeriod);
	}
	
	private void calculateMaxLoanInstallment(LoanDecisionContext context)
	{
		LoanRequest request = context.getRequest();
		
		BigDecimal value1 = request.getMonthlyIncome().subtract(request.getMonthlyLivingCosts()).subtract(request.getMonthlyLoanCosts());
		BigDecimal value2 = context.getConstants().getDti().multiply(request.getMonthlyIncome()).subtract(request.getMonthlyLoanCosts());
		
		context.getOffer().setMaxLoanInstallment(value1.min(value2).setScale(2, RoundingMode.HALF_EVEN));
	}
	
	private void calculateMaxLoanAmount(LoanDecisionContext context)
	{
		LoanRequest request = context.getRequest();
		LoanDecisionConstants constants = context.getConstants();
		
		BigDecimal value1 = constants.getMaxTotalLoanAmount().subtract(request.getTotalLoanBalances());
		BigDecimal value2 = constants.getMaxLoanAmount();
		
		BigDecimal mi = constants.getInterest().divide(BigDecimal.valueOf(12), 5, RoundingMode.HALF_EVEN);
		BigDecimal value3factor = BigDecimal.ONE.subtract((BigDecimal.ONE.divide(BigDecimal.ONE.add(mi), 5, RoundingMode.HALF_EVEN).pow(context.getOffer().getMaxLoanPeriod())))
				.divide(mi);
		
		BigDecimal value3 = context.getOffer().getMaxLoanInstallment().multiply(value3factor);
		
		context.getOffer().setMaxLoanAmount(value1.min(value2).min(value3).setScale(2, RoundingMode.HALF_EVEN));
	}
	
	@Data
	private static class LoanDecisionContext
	{
		private final LoanRequest request;
		private final LoanOffer offer;
		private final LoanDecisionConstants constants;
	}
}
