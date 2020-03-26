package com.template.contracts;

import com.sun.istack.NotNull;
import com.template.states.TemplateState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.dom4j.IllegalAddException;

import java.security.PublicKey;
import java.util.List;

// ************
// * Contract *
// ************
public class TemplateContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.TemplateContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(@NotNull final LedgerTransaction tx) throws IllegalArgumentException {

    if(tx.getCommands().size() != 1){
        throw new IllegalArgumentException("Command input can be only one") ;
    }

    Command command = tx.getCommand(0);
    CommandData commandType = command.getValue();
    List<PublicKey> reqSigners = command.getSigners();
    System.out.println("command "+ command);
    System.out.println("commandType = " + commandType);
    System.out.println("Required Signer " + reqSigners);

        if(commandType instanceof LoanRequest ){

            //Shape Rule

            if(tx.getInputStates().size() != 0) {
                throw new IllegalArgumentException("No input in LoanRequest ");
            }
            if(tx.getOutputs().size() != 1) {
                throw new IllegalAddException("There can be only one output");
            }
            //    Content Rules

           ContractState outputState = tx.getOutput(0);

            if (!(outputState instanceof TemplateState )) {
                throw new IllegalArgumentException("Output state has to be of TemplateState");
            }
            TemplateState loanState = (TemplateState) outputState;
            System.out.println("Loan Amount received: " + loanState.getLoanAmt());
            System.out.println("Credit Score" + loanState.getCreditScore() );
            if(! (loanState.getLoanAmt() >= 10000 && loanState.getLoanAmt() <= 500000)) {
                throw new IllegalArgumentException("Loan amount can be between 10,000 and 50,000");
            }

            if(! (loanState.getCreditScore() >= 500)) {
                throw new IllegalArgumentException("Loan is rejected,Credit Score shoud be greter than 500");
            }


            //    Signer Rules

            PublicKey moneyTapKey = loanState.getMoneyTap().getOwningKey();

            if ((!reqSigners.contains(moneyTapKey))) {
                throw new IllegalArgumentException("MoneyTap party must sign the transaction");
            }
        }
    }

    // Used to indicate the transaction's intent.
    public static class LoanRequest implements CommandData {

    }

   /* public interface Commands extends CommandData {
        class Action implements Commands {}
    }*/
}