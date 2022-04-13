package com.gurusinga.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gurusinga.demo.model.Account
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class AccountControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
){
    var baseUrl: String = "/api/accounts"

    @Nested
    @DisplayName("GET /api/accounts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAccounts {
        @Test
        fun `should return all accounts`() {
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].userName") { value("jdoe") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/accounts/{userName}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAccount {
        @Test
        fun `should return the account with the given username`() {
            val userName = "jdoe"
            mockMvc.get("$baseUrl/$userName")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.name") { value("John Doe") }
                    jsonPath("$.emailAddress") { value("jdoe@google.de") }
                }
        }

        @Test
        fun `should return NotFound when the username is missing`() {
            val userName = "does_not_exist"
            mockMvc.get("$baseUrl/$userName")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/accounts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewAccount {
        @Test
        fun `should add a new account`() {
            val newAccount = Account("newAccount", "New Account", "new@account.com")

            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newAccount)
            }

            performPost.andDo { print() }
            performPost.andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(newAccount))
                }
            }

            mockMvc.get("${baseUrl}/${newAccount.userName}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(newAccount))
                    }
                }
        }

        @Test
        fun `should return BadRequest when account already exists`() {
            val currentAccount = Account("jdoe", "John Doe", "jdoe@google.de")

            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(currentAccount)
            }

            performPost.andDo { print() }
            performPost.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @Nested
    @DisplayName("PATCH /api/accounts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingAccount {
        @Test
        fun `should update an existing account`() {
            val updatedAccount = Account("jdoe", "Not John Doe", "jdoe@google.de")

            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedAccount)
            }

            performPatch.andDo { print() }
            performPatch.andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(updatedAccount))
                }
            }

            mockMvc.get("${baseUrl}/${updatedAccount.userName}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updatedAccount))
                    }
                }
        }

        @Test
        fun `should return NotFound if there is no account is defined`() {
            val updatedAccount = Account("notFoundAccount", "Not Found", "not@found.de")

            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedAccount)
            }

            performPatch.andDo { print() }
            performPatch.andExpect {
                status { isNotFound() }
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/accounts")
    @DirtiesContext
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingAccount{
        @Test
        fun `should delete an existing account`() {
            val userName = "jdoe"
            val performRemoval = mockMvc.delete("$baseUrl/$userName")

            performRemoval.andDo { print() }
            performRemoval.andExpect {
                status { isNoContent() }
            }

            mockMvc.get("$baseUrl/1234")
                .andExpect { status { isNotFound() } }

        }

        @Test
        fun `should return NotFound if there is no account is defined`() {

            val performRemoval = mockMvc.delete("$baseUrl/does_not_exist")
            performRemoval.andDo { print() }
            performRemoval.andExpect {
                status { isNotFound() }
            }
        }
    }
}