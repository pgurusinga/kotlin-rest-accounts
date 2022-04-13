package com.gurusinga.demo.service

import com.gurusinga.demo.datasource.UserDataSource
import com.gurusinga.demo.model.Account
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class AccountService(private val dataSource: UserDataSource) {
    fun getAccounts(): Collection<Account> = dataSource.retrieveAccounts()
    fun getAccount(userName: String): Account = dataSource.retrieveAccount(userName)
    fun addAccount(account: Account): Account = dataSource.createAccount(account)
    fun updateAccount(account: Account): Account = dataSource.updateAccount(account)
    fun removeAccount(accountNumber: String): Unit = dataSource.removeAccount(accountNumber)
}