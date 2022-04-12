package com.gurusinga.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gurusinga.demo.model.Bank
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
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
){
    var baseUrl: String = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all bank`() {
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber") { value("1234") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {

            val accountNumber = 1234
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("12.3") }
                    jsonPath("$.transactionFee") { value("12") }
                }
        }

        @Test
        fun `should return NotFound when the accountNumber is missing`() {
            val accountNumber = "does_not_exist"

            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add a new bank`() {
            val newAccount = Bank("9999", 9.9, 99)

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

            mockMvc.get("${baseUrl}/${newAccount.accountNumber}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(newAccount))
                    }
                }
        }

        @Test
        fun `should return BadRequest when bank already exists`() {
            val currentAccount = Bank("1234",  12.3, 12)

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
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an existing bank`() {
            val updatedAccount = Bank("1234", 1.0, 1)

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

            mockMvc.get("${baseUrl}/${updatedAccount.accountNumber}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updatedAccount))
                    }
                }
        }

        @Test
        fun `should return NotFound if there is no account number is defined`() {
            val updatedAccount = Bank("444444", 1.0, 1)

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
    @DisplayName("DELETE /api/banks")
    @DirtiesContext
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {
        @Test
        fun `should delete an existing bank`() {

            val performRemoval = mockMvc.delete("$baseUrl/1234")

            performRemoval.andDo { print() }
            performRemoval.andExpect {
                status { isNoContent() }
            }

            mockMvc.get("$baseUrl/1234")
                .andExpect { status { isNotFound() } }

        }

        @Test
        fun `should return NotFound if there is no account number is defined`() {

            val performRemoval = mockMvc.delete("$baseUrl/does_not_exist")
            performRemoval.andDo { print() }
            performRemoval.andExpect {
                status { isNotFound() }
            }
        }
    }
}