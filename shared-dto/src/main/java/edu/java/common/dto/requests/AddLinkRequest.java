package edu.java.common.dto.requests;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(@NotNull URI link) {}
