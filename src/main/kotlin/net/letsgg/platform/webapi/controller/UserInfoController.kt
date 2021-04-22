package net.letsgg.platform.webapi.controller

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.mapper.LetsggUserMapper
import net.letsgg.platform.security.AppUserDetails
import net.letsgg.platform.security.CurrentUser
import net.letsgg.platform.security.Preauthorized
import net.letsgg.platform.service.user.AppUserService
import net.letsgg.platform.webapi.dto.AppUserResponseDto
import net.letsgg.platform.webapi.dto.UserFinishSetupModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/user-info")
//@CrossOrigin(origins = ["*"], allowedHeaders = ["*"], exposedHeaders = ["*"])
class UserInfoController(
    private val userService: AppUserService,
    private val userMapper: LetsggUserMapper,
) {

    @GetMapping("/me")
    @PreAuthorize(Preauthorized.WITH_AUTHORITY_USER_INFO)
    fun getCurrentUser(@CurrentUser email: String): ResponseEntity<AppUserResponseDto> {
        val user = userService.getByEmail(email)
        return ResponseEntity(userMapper.toDto(user), HttpStatus.OK)
    }

    @RestController
    @RequestMapping("api/user-info/finish-setup")
    @PreAuthorize(Preauthorized.WITH_ROLE_UNFINISHED_SETUP_USER)
    internal class UserFinishSetupController(
        private val userService: AppUserService
    ) {
        
        fun finishSetup(@CurrentUser userEmail: String, @RequestBody finishSetupBody: UserFinishSetupModel) {
            //update user fields with {@code finishSetupBody} ones. also do not forget to change {@code user_role}
            TODO()
        }
    }
}