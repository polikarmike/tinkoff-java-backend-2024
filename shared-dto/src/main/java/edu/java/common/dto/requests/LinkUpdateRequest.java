package edu.java.common.dto.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(@NotNull Long id,
                                @NotNull URI url,
                                @NotBlank String description,
                                @NotNull List<Long> tgChatIds){
}
