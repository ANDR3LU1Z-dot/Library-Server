package br.pucpr.libraryserver.roles

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.intellij.lang.annotations.Pattern

@Entity
class Role (
    @Id
    @Pattern("ˆ[A-Z][A-Z0-9]+$")
    var name: String,
    var description: String,
)