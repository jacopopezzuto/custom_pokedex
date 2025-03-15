package com.pokemon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonResponse {
    private String name;
    private String description;
    private String habitat;
    @JsonProperty("legendary")
    private boolean isLegendary;
}
