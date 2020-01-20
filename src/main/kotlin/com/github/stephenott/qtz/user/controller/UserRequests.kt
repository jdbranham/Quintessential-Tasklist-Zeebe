package com.github.stephenott.qtz.user.controller

import javax.validation.constraints.NotBlank

class CreateUserRequest (
    @NotBlank val username: String,
    @NotBlank val password: String,
    val roles: Set<String>
)

class CreateRoleRequest (
    @NotBlank val authority: String
)