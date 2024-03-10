package edu.java.common.dto.requests;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;

public record AddLinkRequest(@NotBlank URI link) {}
