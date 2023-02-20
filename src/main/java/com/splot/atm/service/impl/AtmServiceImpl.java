package com.splot.atm.service.impl;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.model.Atm;
import com.splot.atm.model.AtmDenomination;
import com.splot.atm.model.Denomination;
import com.splot.atm.repository.AtmDenominationRepository;
import com.splot.atm.repository.AtmRepository;
import com.splot.atm.repository.DenominationRepository;
import com.splot.atm.service.AtmService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;
    private final DenominationRepository denominationRepository;
    private final AtmDenominationRepository atmDenominationRepository;

    public AtmServiceImpl(AtmRepository atmRepository,
                          DenominationRepository denominationRepository,
                          AtmDenominationRepository atmDenominationRepository) {
        this.atmRepository = atmRepository;
        this.denominationRepository = denominationRepository;
        this.atmDenominationRepository = atmDenominationRepository;
    }

    @Override
    public Atm save(Atm atm) {
        return atmRepository.save(atm);
    }

    @Override
    public Atm findById(Long id) {
        return atmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can`t find ATM with id: " + id));
    }

    @Override
    public List<Atm> findAll() {
        return atmRepository.findAll();
    }

    @Override
    public Atm createAtm(String name) {
        Atm atm = new Atm();
        atm.setName(name);
        return atmRepository.save(atm);
    }

    @Override
    @Transactional
    public AtmDenomination addDenomination(Long id, int value, int numberOfNotes) {
        List<Integer> denominationValues = denominationRepository.findAll()
                .stream()
                .map(Denomination::getNominal).collect(Collectors.toList());
        if (!denominationValues.contains(value)) {
            throw new RuntimeException("Not allowed value: "
                    + value + ", use only 100, 200 or 500.");
        }

        Denomination denomination = denominationRepository.findByNominal(value);
        Atm atm = atmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ATM with id: " + id + " not found."));
        AtmDenomination atmDenomination = new AtmDenomination();
        atmDenomination.setAtm(atm);
        atmDenomination.setDenomination(denomination);
        atmDenomination.setNumberOfNotes(numberOfNotes);
        atmDenominationRepository.save(atmDenomination);
        return atmDenomination;
    }

    @Override
    @Transactional
    public AtmDenomination updateDenomination(Long atmId,
                                              Long atmDenominationId, int numberOfNotes) {
        AtmDenomination atmDenomination = atmDenominationRepository.findById(atmDenominationId)
                .orElseThrow(() -> new RuntimeException("ATM denomination not found"));
        atmDenomination.setNumberOfNotes(numberOfNotes);
        return atmDenominationRepository.save(atmDenomination);
    }

    @Override
    @Transactional
    public List<AtmDenomination> addDeposit(Long atmId, DepositRequestDto deposit) {
        List<AtmDenomination> all = atmDenominationRepository.findAllByAtm_Id(atmId);
        for (AtmDenomination denomination : all) {
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
    public List<AtmDenomination> withdraw(Long atmId, BigDecimal amount) {
        List<AtmDenomination> all = atmDenominationRepository.findAllByAtm_Id(atmId);
        Map<Integer, AtmDenomination> denominations = new HashMap<>();
        for (AtmDenomination denomination : all) {
            denominations.put(denomination.getDenomination().getNominal(), denomination);
        }

        BigDecimal remainingAmount = amount;
        for (int denominationValue : new int[]{500, 200, 100}) {
            AtmDenomination denomination = denominations.get(denominationValue);
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

        return atmDenominationRepository.saveAll(denominations.values());
    }

}
