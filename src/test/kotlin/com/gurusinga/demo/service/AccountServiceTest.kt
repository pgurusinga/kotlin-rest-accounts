package com.gurusinga.demo.service

import com.gurusinga.demo.datasource.UserDataSource
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class AccountServiceTest {
    private val dataSource: UserDataSource = mockk(relaxed = true)
    private val accountService = AccountService(dataSource)

    @Test
    fun `should call its data source to retrieve accounts`() {

        accountService.getAccounts()
        verify(exactly = 1) { dataSource.retrieveAccounts() }
        confirmVerified(dataSource)
    }
}
