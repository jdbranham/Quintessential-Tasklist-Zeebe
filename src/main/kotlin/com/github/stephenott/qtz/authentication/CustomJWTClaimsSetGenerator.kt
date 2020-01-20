package com.github.stephenott.qtz.authentication

import com.nimbusds.jwt.JWTClaimsSet
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Replaces
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.config.TokenConfiguration
import io.micronaut.security.token.jwt.generator.claims.ClaimsAudienceProvider
import io.micronaut.security.token.jwt.generator.claims.JWTClaimsSetGenerator
import io.micronaut.security.token.jwt.generator.claims.JwtIdGenerator
import net.minidev.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Replaces(JWTClaimsSetGenerator::class)
class CustomJWTClaimsSetGenerator(
        var securityConfiguration: SecurityConfiguration,
        tokenConfiguration: TokenConfiguration, jwtIdGenerator: JwtIdGenerator?, claimsAudienceProvider: ClaimsAudienceProvider? ) :
        JWTClaimsSetGenerator(tokenConfiguration, jwtIdGenerator, claimsAudienceProvider) {

    @set:Inject
    @setparam:Property(name = "micronaut.security.token.roles-name")
    var rolesClaim: String = "roles"


    override fun populateWithUserDetails(builder: JWTClaimsSet.Builder, userDetails: UserDetails) {
        super.populateWithUserDetails(builder, userDetails)
        val attributes: Map<String, Any> = userDetails.getAttributes(rolesClaim, userDetails.username)

        val roles = attributes.getValue(rolesClaim)
        val effectiveRoles = JSONArray()
        // Add user roles to effective roles
        if(roles is Collection<*>) {
            roles.map { sanitizeRoleName(it.toString()) }.apply {
                effectiveRoles.addAll(this)
            }
        }
        // Add role-permissions mappings to effective roles
        securityConfiguration.rolePermissions.map { rp ->
            if(effectiveRoles.contains(rp.role)) {
                rp.permissions.map {
                    if (!effectiveRoles.contains(it.toString())){
                        effectiveRoles.add(it.toString())
                    }
                }
            }
        }
        // put effective roles in jwt
        builder.claim("roles", effectiveRoles)
    }

    private fun sanitizeRoleName(value: String): String {
        return if (value == null) {
            "INVALID_VALUE"
        } else {
            val role: String = value.toUpperCase().trim().replace(" ", "_")
            return if (role.startsWith("ROLE_")) {
                role;
            } else {
                "ROLE_$role"
            }
        }
    }


}