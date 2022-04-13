package com.gurusinga.demo.datasource.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MockAccountDataSourceTest {
    private val mockDataSource = MockUserDataSource()

    @Test
    fun `should provide a collection of account`() {
        val accounts = mockDataSource.retrieveAccounts()
        assertThat(accounts.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `should provide some mock data`() {
        val accounts = mockDataSource.retrieveAccounts()
        assertThat(accounts).allMatch { it.userName.isNotBlank() }
        assertThat(accounts).anyMatch { it.name.isNotBlank() }
        assertThat(accounts).anyMatch { it.emailAddress.isNotBlank() }
    }
}