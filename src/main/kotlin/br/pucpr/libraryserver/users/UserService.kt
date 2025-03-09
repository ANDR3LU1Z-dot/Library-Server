package br.pucpr.libraryserver.users


import br.pucpr.libraryserver.expection.BadRequestException
import br.pucpr.libraryserver.expection.NotFoundException
import br.pucpr.libraryserver.libraries.LibraryService
import br.pucpr.libraryserver.roles.RoleService
import br.pucpr.libraryserver.security.JWT
import br.pucpr.libraryserver.users.controller.responses.LoginResponse
import br.pucpr.libraryserver.users.controller.responses.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service //Dizendo ao spring boot que isso é um bean
class UserService(
    val userRepository: UserRepository,
    val roleService: RoleService,
    val libraryService: LibraryService,
    private val jwt: JWT
) {
    fun insert(user: User) = userRepository.save(user)

    fun findAll(dir: SortDir, role: String?): List<User> {
        if (!role.isNullOrBlank())
            return userRepository.findByRole(role)
        return when (dir) {
            SortDir.ASC -> userRepository.findAll(Sort.by("name"))
            SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
        }

    }
    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)
    fun delete(id: Long) = userRepository.deleteById(id)

    fun addRole(id: Long, roleName: String): Boolean {
        val roleUpper = roleName.uppercase()
        val user = findByIdOrNull(id) ?: throw NotFoundException("User $id not found")
        if (user.roles.any{it.name == roleUpper}) return false

        val role = roleService.findByNameOrNull(roleUpper.uppercase())
            ?: throw BadRequestException("Role ${roleUpper} not found")

        user.roles.add(role)
        userRepository.save(user)
        return true
    }

    fun login(email: String, password: String): LoginResponse? {
        val user = userRepository.findByEmail(email) ?: return null
        if(user.password != password) return null
        log.info("User Logger i. id=${user.id}, name=${user.name}")
        return LoginResponse(
            token = jwt.createToken(user),
            user = UserResponse(user)
        )
    }

    fun addLibrary(id: Long, libraryId: Long): Boolean {
        val user = findByIdOrNull(id) ?: throw NotFoundException("User id: $id not found")
        val library = libraryService.findByIdOrNull(libraryId) ?: throw NotFoundException("Library id: $id not found")
        if(user.library != null) return false
        user.library = library
        userRepository.save(user)
        return true
    }

    fun removeLibrary(id: Long, libraryId: Long): Boolean {
        val user = findByIdOrNull(id) ?: throw NotFoundException("User $id not found")
        val userLibId = user.library?.id
        if(user.library == null || userLibId != libraryId) return false
        user.library = null
        userRepository.save(user)
        return true
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}