package br.pucpr.libraryserver.users.controller.responses

data class LoginResponse(
    val token: String,
    val user: UserResponse
)
