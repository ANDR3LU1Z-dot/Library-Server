package br.pucpr.libraryserver

import br.pucpr.libraryserver.roles.Role
import br.pucpr.libraryserver.roles.RoleRepository
import br.pucpr.libraryserver.users.User
import br.pucpr.libraryserver.users.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    @Qualifier("defaultAdmin") private val adminUser: User
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole =
            roleRepository.findByIdOrNull("ADMIN")
                ?: roleRepository.save(Role("ADMIN", "System Administrator"))
                    .also { roleRepository.save(Role("USER", "Premium User")) }
        if (userRepository.findByRole("ADMIN").isEmpty()) {
            val admin = User(
                email = "admin@autserver.com",
                password = "admin",
                name = "Auth Server Administrator"
            )
            adminUser.roles.add(adminRole)
            userRepository.save(adminUser)
        }
    }
}