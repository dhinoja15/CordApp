package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(Initiator.class)
public class Responder extends FlowLogic<SignedTransaction> {
    private FlowSession counterpartySession;

    public Responder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Responder flow logic goes here.
        System.out.println("Loan Request received from MoneyTap: " + counterpartySession.getCounterparty().getName().getOrganisation());

        String receivedMessage = counterpartySession.receive(String.class).unwrap( s -> s);
        System.out.println("Message received: " + receivedMessage);
        counterpartySession.send(receivedMessage);


        System.out.println("cc====" );

        return subFlow(new ReceiveFinalityFlow(counterpartySession));

    }
}
