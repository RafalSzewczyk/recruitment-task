package pl.rafsze.recruitment.task;

import java.util.List;

public interface LoanOfferService
{
	List<LoanOffer> getLoanOffers(LoanRequest request, PeriodDependentValue<LoanDecisionConstants> parameters);
}
