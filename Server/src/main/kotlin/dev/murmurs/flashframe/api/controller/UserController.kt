package dev.murmurs.flashframe.api.controller

import dev.murmurs.server.web.api.UsersApi
import dev.murmurs.server.web.model.UserInfoView
import mu.KotlinLogging
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

private val logger = KotlinLogging.logger {}

@RestController
class UserController : UsersApi {
    override fun usersUsernameGet(username: String, principal: Principal): UserInfoView {
        logger.info { "Cognito Princial name ${principal.name}" }
        return UserInfoView("test")
    }
}
