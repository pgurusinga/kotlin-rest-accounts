package com.gurusinga.demo.controller

import com.gurusinga.demo.model.Account
import com.gurusinga.demo.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val service: AccountService){

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> = ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> = ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getAccounts(): Collection<Account> = service.getAccounts()

    @GetMapping("/{userName}")
    fun getAccount(@PathVariable userName: String) = service.getAccount(userName)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addAccount(@RequestBody account: Account) = service.addAccount(account)

    @PatchMapping
    fun updateAccount(@RequestBody account: Account) = service.updateAccount(account)

    @DeleteMapping("/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeAccount(@PathVariable userName: String): Unit = service.removeAccount(userName)
}