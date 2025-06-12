package com.tecno.aging.domain.models.DTO

data class VerifyJwtResponse(
    val valid: Boolean,
    val message: String?
)