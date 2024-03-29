package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImp implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<TransactionDTO> getTransactionsDTO(List<Transaction> transactions) {
        return transactions
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
