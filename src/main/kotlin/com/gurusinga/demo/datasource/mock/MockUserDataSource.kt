package com.gurusinga.demo.datasource.mock

import org.springframework.stereotype.Repository
import com.gurusinga.demo.datasource.UserDataSource
import com.gurusinga.demo.model.Account

@Repository
class MockUserDataSource : UserDataSource {
    val user = mutableListOf(
        Account("jdoe", "John Doe", "jdoe@google.de"),
        Account("foobar", "Foo Bar", "foooo@bar.com"),
        Account("anim", "Max Mustermann", "madmax@cool.io")
    )
    override fun retrieveAccounts(): Collection<Account> = user
    override fun retrieveAccount(userName: String) =
        user.firstOrNull { it.userName == userName }
            ?: throw NoSuchElementException("Missing accountNumber $userName")

    override fun createAccount(account: Account): Account {
        if (user.any { it.userName == account.userName }) {
            throw IllegalArgumentException("Account Number already exists")
        }
        user.add(account)
        return account
    }

    override fun updateAccount(account: Account): Account {
        val currentAccount = user.firstOrNull() {
            it.userName == account.userName
        } ?: throw NoSuchElementException("Could not patch for invalid $account.accountNumber")

        user.remove(currentAccount)
        user.add(account)
        return account
    }

    override fun removeAccount(userName: String): Unit {
        val currentAccount = user.firstOrNull() {
            it.userName == userName
        } ?: throw NoSuchElementException("Could not remove for invalid $userName")

        user.remove(currentAccount)
    }
}