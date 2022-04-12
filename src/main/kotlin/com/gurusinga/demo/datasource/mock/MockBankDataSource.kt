package com.gurusinga.demo.datasource.mock

import org.springframework.stereotype.Repository
import com.gurusinga.demo.datasource.BankDataSource
import com.gurusinga.demo.model.Bank

@Repository
class MockBankDataSource : BankDataSource {
    val banks = mutableListOf(
        Bank("1234", 12.3, 12),
        Bank("3456", 120.0, 1),
        Bank("5678", 22.3, 4)
    )
    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber: String) =
        banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Missing accountNumber $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Account Number already exists")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull() {
            it.accountNumber == bank.accountNumber
        } ?: throw NoSuchElementException("Could not patch for invalid $bank.accountNumber")

        banks.remove(currentBank)
        banks.add(bank)
        return bank
    }

    override fun removeBank(accountNumber: String): Unit {
        val currentBank = banks.firstOrNull() {
            it.accountNumber == accountNumber
        } ?: throw NoSuchElementException("Could not remove for invalid $accountNumber")

        banks.remove(currentBank)
    }
}