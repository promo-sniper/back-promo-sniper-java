package com.anorneto.promosniper.presenters.controller;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import com.anorneto.promosniper.infrastructure.repositories.PromoRepository;
import com.anorneto.promosniper.presenters.common.CommonApiResponse;
import com.codahale.metrics.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * @author Anor Neto
 */
@Path("/promo")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Promo", description = "Promo related routes")
public class PromoController {
    final PromoRepository promoRepository;
    final Jdbi jdbi;

    // TODO: Do dependency injection here later
    public PromoController(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.promoRepository = new PromoRepository(jdbi);
    }

    @GET
    @Timed
    @Operation(
            summary = "Get Promos for Source",
            description = "Get Promos for Source",
            operationId = "getPromosForSource",
            responses = {@ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)})
    public CommonApiResponse<List<PromoDTO>> getTelegramChannels(
            @Parameter(description = "Source to search promotions for", required = true)
                    @QueryParam("sourceName")
                    @NotBlank
                    String sourceName) {
        CommonApiResponse<List<PromoDTO>> response = new CommonApiResponse<>();
        List<PromoDTO> promoDTOList = promoRepository.getForSouceName(sourceName);
        return response.ok(promoDTOList);
    }
}