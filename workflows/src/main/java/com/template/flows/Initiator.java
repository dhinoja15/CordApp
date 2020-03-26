package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.TemplateContract;
import com.template.states.TemplateState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import static com.template.contracts.TemplateContract.ID;

import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class Initiator extends FlowLogic<SignedTransaction> {

    private String custName;
    private String custSSN;
    private int creditScore;
    private Long  loanAmt;
    private Party rblBank;


    public Initiator(String custName, String custSSN, int creditScore,Long loanAmt, Party rblBank) {
        this.custName = custName;
        this.custSSN = custSSN;
        this.creditScore = creditScore;
        this.loanAmt = loanAmt;
        this.rblBank = rblBank;
    }



    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Initiator flow logic goes here.
        if (getOurIdentity().getName().getOrganisation().equals("MoneyTap")) {
            System.out.println("MoneyTap Identity Verified!");
        } else {
            throw new FlowException("Transaction not initiated by MoneyTap");
        }//        Get Notary identity from network map

        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

//        Create the elements for a transaction (Input/ Output states)

        TemplateState outputState = new TemplateState(custName, custSSN, creditScore, loanAmt, getOurIdentity(),rblBank);

//        Transations in Corda are built using Transaction Builder and elements are added to it

        TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(outputState,ID)
                .addCommand(new TemplateContract.LoanRequest(),getOurIdentity().getOwningKey());

        //        Sign the transation

        SignedTransaction loanReqTxn = getServiceHub().signInitialTransaction(txBuilder);

//        Create a session with Bank

        FlowSession loanReqSession = initiateFlow(rblBank);

//        Finalize the transaction

        return subFlow(new FinalityFlow(loanReqTxn, loanReqSession));


    }
}
