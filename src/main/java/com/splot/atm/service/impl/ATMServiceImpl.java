package com.splot.atm.service.impl;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.model.ATM;
import com.splot.atm.model.ATMDenomination;
import com.splot.atm.model.Denomination;
import com.splot.atm.repository.ATMDenominationRepository;
import com.splot.atm.repository.ATMRepository;
import com.splot.atm.repository.DenominationRepository;
import com.splot.atm.service.ATMService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ATMServiceImpl implements ATMService {
    private final ATMRepository atmRepository;
    private final DenominationRepository denominationRepository;
    private final ATMDenominationRepository atmDenominationRepository;

    public ATMServiceImpl(ATMRepository atmRepository,
                          DenominationRepository denominationRepository,
                          ATMDenominationRepository atmDenominationRepository) {
        this.atmRepository = atmRepository;
        this.denominationRepository = denominationRepository;
        this.atmDenominationRepository = atmDenominationRepository;
    }

    @Override
    public ATM findById(Long id) {
        return atmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ATM with id: " + id + " not found."));
    }

    @Override
    public ATM createATM(String name) {
        ATM atm = new ATM();
        atm.setName(name);
        return atmRepository.save(atm);
    }

    @Override
    @Transactional
    public ATMDenomination addDenomination(Long id, int value, int numberOfNotes) {
        List<Integer> denominationValues = denominationRepository.findAll()
                .stream()
                .map(Denomination::getNominal).collect(Collectors.toList());
        if (!denominationValues.contains(value)) {
            throw new RuntimeException("Not allowed value: " + value +", use only 100, 200 or 500.");
        }

        Denomination denomination = denominationRepository.findByNominal(value);
        ATM atm = atmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ATM with id: " + id + " not found."));
        ATMDenomination atmDenomination = new ATMDenomination();
        atmDenomination.setAtm(atm);
        atmDenomination.setDenomination(denomination);
        atmDenomination.setNumberOfNotes(numberOfNotes);
        atmDenominationRepository.save(atmDenomination);
        return atmDenomination;
    }

    @Override
    @Transactional
    public ATMDenomination updateDenomination(Long atmId, Long atmDenominationId, int numberOfNotes) {
        ATMDenomination atmDenomination = atmDenominationRepository.findById(atmDenominationId)
                .orElseThrow(() -> new RuntimeException("ATM denomination not found"));
        atmDenomination.setNumberOfNotes(numberOfNotes);
        return atmDenominationRepository.save(atmDenomination);
    }

    @Override
    @Transactional
    public List<ATMDenomination> addDeposit(Long atmId, DepositRequestDto deposit) {
        List<ATMDenomination> all = atmDenominationRepository.findAllByAtm_Id(atmId);
        for (ATMDenomination denomination : all) {
            if (denomination.getDenomination().getNominal() == 100) {
                denomination.setNumberOfNotes(denomination.getNumberOfNotes()
                        + deposit.getNumberOfHundredNotes());
            } else if (denomination.getDenomination().getNominal() == 200) {
                denomination.setNumberOfNotes(denomination.getNumberOfNotes()
                        + deposit.getNumberOfTwoHundredNotes());
            } else {
                denomination.setNumberOfNotes(denomination.getNumberOfNotes()
                        + deposit.getNumberOfFiveHundredNotes());
            }
        }
        return atmDenominationRepository.saveAll(all);
    }

    @Override
    @Transactional
    public List<ATMDenomination> withdraw(Long atmId, BigDecimal amount) {
        List<ATMDenomination> all = atmDenominationRepository.findAllByAtm_Id(atmId);
        Map<Integer, ATMDenomination> denominations = new HashMap<>();
        for (ATMDenomination denomination : all) {
            denominations.put(denomination.getDenomination().getNominal(), denomination);
        }

        BigDecimal remainingAmount = amount;
        for (int denominationValue : new int[]{500, 200, 100}) {
            ATMDenomination denomination = denominations.get(denominationValue);
            if (denomination != null) {
                int notesToWithdraw = remainingAmount
                        .divideToIntegralValue(BigDecimal.valueOf(denominationValue)).intValue();
                if (notesToWithdraw > denomination.getNumberOfNotes()) {
                    notesToWithdraw = denomination.getNumberOfNotes();
                }
                remainingAmount = remainingAmount
                        .subtract(BigDecimal.valueOf((long) notesToWithdraw * denominationValue));
                denomination.setNumberOfNotes(denomination.getNumberOfNotes() - notesToWithdraw);
            }
        }

        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Not enough money in ATM");
        }

        List<ATMDenomination> updatedDenominations
                = atmDenominationRepository.saveAll(denominations.values());
        // Check if the withdrawal was successful before returning the updated denominations
        if (amount.compareTo(getWithdrawnAmount(updatedDenominations)) != 0) {
            throw new RuntimeException("Withdrawal failed. Database updated with incorrect information.");
        }
        return updatedDenominations;
    }

    private BigDecimal getWithdrawnAmount(List<ATMDenomination> denominations) {
        BigDecimal amount = BigDecimal.ZERO;
        for (ATMDenomination denomination : denominations) {
            amount = amount.add(BigDecimal
                    .valueOf((long) denomination.getNumberOfNotes()
                            * denomination.getDenomination().getNominal()));
        }
        return amount;
    }
}
