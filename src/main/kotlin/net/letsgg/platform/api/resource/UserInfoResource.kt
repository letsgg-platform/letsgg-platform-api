package net.letsgg.platform.api.resource

import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.api.dto.UserFinishSetupModel
import net.letsgg.platform.api.mapper.LetsggUserMapper
import net.letsgg.platform.api.view.Views.Response
import net.letsgg.platform.exception.handler.ApiError
import net.letsgg.platform.security.CurrentUser
import net.letsgg.platform.security.Preauthorized
import net.letsgg.platform.service.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/user-info")
class UserInfoResource(
    private val userService: UserService,
    private val userMapper: LetsggUserMapper,
) {

    @Operation(summary = "Get Currently Logged-In User")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDto::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401", description = "Login Required",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiError::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/me")
    @PreAuthorize(Preauthorized.WITH_AUTHORITY_USER_INFO)
    @JsonView(Response::class)
    fun getCurrentUser(@CurrentUser email: String): ResponseEntity<UserDto> {
        val user = userService.getByEmail(email)
        return ResponseEntity(userMapper.convert(user), HttpStatus.OK)
    }

    @RestController
    @RequestMapping("api/user-info/finish-setup")
    @PreAuthorize(Preauthorized.WITH_ROLE_UNFINISHED_SETUP_USER)
    internal class UserFinishSetupController(
        private val userService: UserService
    ) {

        fun finishSetup(@CurrentUser userEmail: String, @RequestBody finishSetupBody: UserFinishSetupModel) {
            // update user fields with {@code finishSetupBody} ones. also do not forget to change {@code user_role}
            TODO()
        }
    }
}
