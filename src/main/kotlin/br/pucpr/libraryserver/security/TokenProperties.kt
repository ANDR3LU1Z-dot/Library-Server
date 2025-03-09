package br.pucpr.libraryserver.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("security.token")
data class TokenProperties @ConstructorBinding constructor (
    val secret: String,
    val issuer: String,
    val expireHours: Long,
    val adminExpireHours: Long
)
