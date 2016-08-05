package com.mifos.mifosxdroid.online.loanrepayment;

import com.mifos.mifosxdroid.base.MvpView;
import com.mifos.objects.accounts.loan.LoanRepaymentResponse;
import com.mifos.objects.templates.loans.LoanRepaymentTemplate;

/**
 * Created by Rajan Maurya on 8/6/16.
 */
public interface LoanRepaymentMvpView extends MvpView {

    void showLoanRepayTemplate(LoanRepaymentTemplate loanRepaymentTemplate);

    void showPaymentSubmittedSuccessfully(LoanRepaymentResponse loanRepaymentResponse);

    void checkLoanRepaymentStatusInDatabase();

    void showLoanRepaymentExistInDatabase(String s);

    void showLoanRepaymentDoesNotExistInDatabase();

    void showFetchingError(String s);
}
