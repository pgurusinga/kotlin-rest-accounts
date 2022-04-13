package com.gurusinga.demo.datasource

import com.gurusinga.demo.model.Account

interface UserDataSource {
    fun retrieveAccounts(): Collection<Account>
    fun retrieveAccount(userName: String): Account
    fun createAccount(account: Account): Account
    fun updateAccount(account: Account): Account
    fun removeAccount(userName: String): Unit
}