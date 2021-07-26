package net.letsgg.platform.api.resource

import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.api.mapper.UserDtoMapper
import net.letsgg.platform.api.view.Views.*
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.exception.handler.ApiError
import net.letsgg.platform.security.CurrentUser
import net.letsgg.platform.security.Preauthorized
import net.letsgg.platform.service.UserFinishSetupService
import net.letsgg.platform.service.user.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/user-info")
@Validated
class UserInfoResource(
  private val userService: UserService,
  private val userDtoMapper: UserDtoMapper,
) {

  @Operation(summary = "Update Currently Logged-In User Profile")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = UserDto::class)
          )
        ]
      ),
      ApiResponse(
        responseCode = "401", description = "Login Required",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = ApiError::class)
          )
        ]
      ),
      ApiResponse(
        responseCode = "406", description = "Validation failed for supplied request body",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = ApiError::class)
          )
        ]
      )
    ]
  )
  @PutMapping
  @PreAuthorize(Preauthorized.WITH_ROLE_USER)
  @JsonView(value = [Response::class])
  fun updateUserProfile(
    @CurrentUser email: String,
    @Valid @JsonView(Update::class) @RequestBody updatedUserDto: UserDto
  ): ResponseEntity<UserDto> {
    val updatedUser = userService.update(updatedUserDto, email)
    return ResponseEntity(userDtoMapper.convert(updatedUser), HttpStatus.OK)
  }

  @Operation(summary = "Get Currently Logged-In User")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = UserDto::class)
          )
        ]
      ),
      ApiResponse(
        responseCode = "401", description = "Login Required",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
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
    return ResponseEntity(userDtoMapper.convert(user), HttpStatus.OK)
  }

  @PostMapping("/search")
  @PreAuthorize(Preauthorized.WITH_AUTHORITY_READ)
  fun getFilteredUsersWithPage(pageable: Pageable): ResponseEntity<Page<LetsggUser>> {
    return ResponseEntity(userService.getAll(pageable), HttpStatus.OK)
  }

  @RestController
  @RequestMapping("api/user-info/finish-setup")
  @PreAuthorize(Preauthorized.WITH_ROLE_UNFINISHED_SETUP_USER)
  internal class UserFinishSetupController(
    private val userFinishSetupService: UserFinishSetupService
  ) {

    @Operation(summary = "Finish Current User Setup")
    @ApiResponses(
      value = [
        ApiResponse(
          responseCode = "200",
          content = [
            Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = Schema(implementation = UserDto::class)
            )
          ]
        ),
        ApiResponse(
          responseCode = "401", description = "Login Required",
          content = [
            Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = Schema(implementation = ApiError::class)
            )
          ]
        )
      ]
    )
    @PostMapping
    @JsonView(Response::class)
    fun finishSetup(
      @CurrentUser userEmail: String,
      @JsonView(Update::class) @Valid @RequestBody updatedUserDto: UserDto
    ): ResponseEntity<UserDto> {
      val finishedSetupUser = userFinishSetupService.finishSetup(userEmail, updatedUserDto)
      return ResponseEntity(finishedSetupUser, HttpStatus.OK)
    }
  }
}