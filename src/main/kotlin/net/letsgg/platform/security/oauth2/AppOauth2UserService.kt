package net.letsgg.platform.security.oauth2

import net.bytebuddy.utility.RandomString
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.exception.OAuth2AuthenticationProcessingException
import net.letsgg.platform.exception.ResourceNotFoundException
import net.letsgg.platform.mapper.LetsggUserMapper
import net.letsgg.platform.repository.LetsggUserRepository
import net.letsgg.platform.security.AppUserDetails
import net.letsgg.platform.service.user.AppUserService
import net.letsgg.platform.utility.LoggerDelegate
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

/**
 * Custom OAuth2 service. Used for extracting user info from the access token,
 * which the resource server provides, after successful user login and exchange
 * of authorization code for access token.
 * */
@Service
//@Transactional(noRollbackFor = [ResourceNotFoundException::class])
class AppOauth2UserService(
    private val userService: AppUserService,
    private val userMapper: LetsggUserMapper,
) : DefaultOAuth2UserService() {

    private val logger by LoggerDelegate()

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)
        return try {
            processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo: OAuth2UserInfo = Oauth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.clientRegistration.registrationId,
            oAuth2User.attributes
        )
        if (oAuth2UserInfo.getEmail().isEmpty()) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }
        var user: LetsggUser
        try {
            user = userService.getByEmail(oAuth2UserInfo.getEmail())
            if (user.authProvider != UserAuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId.toUpperCase())) {
                throw OAuth2AuthenticationProcessingException(
                    "Looks like you're signed up with ${user.authProvider} account. Please use your ${user.authProvider} account to login."
                )
            }
        } catch (e: ResourceNotFoundException) {
            logger.info("registering new oauth user with email ${oAuth2UserInfo.getEmail()}")
            user = registerNewUser(
                UserAuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId.toUpperCase()),
                oAuth2UserInfo
            )
        }
        return AppUserDetails(user, oAuth2User.attributes)
    }

    //TODO. consider what fields to include
    private fun registerNewUser(oauth2Provider: UserAuthProvider, oAuth2UserInfo: OAuth2UserInfo): LetsggUser {
        val user = userMapper.toEntity(oAuth2UserInfo, oauth2Provider)
        return userService.save(user)
    }
}