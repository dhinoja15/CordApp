package com.template.states;

import com.template.contracts.TemplateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(TemplateContract.class)
public class TemplateState implements ContractState {

    private final String custName;
    private final String custSSN;
    private final int creditScore;
    private final Long  loanAmt;
    private final Party moneyTap;
    private final Party rblBank;




        public TemplateState(String custName, String custSSN, int creditScore, Long loanAmt, Party moneyTap, Party rblBank) {

        this.custName = custName;
        this.custSSN = custSSN;
        this.creditScore = creditScore;
        this.loanAmt = loanAmt;
        this.moneyTap = moneyTap;
        this.rblBank = rblBank;

    }

    public String getCustName() {
        return custName;
    }

    public String getCustSSN() {
        return custSSN;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public Long getLoanAmt() {
        return loanAmt;
    }

    public Party getMoneyTap() {
        return moneyTap;
    }

    public Party getRblBank() {
        return rblBank;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(moneyTap,rblBank);
    }
}