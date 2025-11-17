package com.scentalux.security;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public record JwtResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("username") String username,
    @JsonProperty("roles") List<String> roles
) {}