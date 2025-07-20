package com.pragma.capabilities.infraestructure.input.res;


import com.pragma.capabilities.application.dto.request.CapabilityRequest;
import com.pragma.capabilities.application.dto.request.PageRequest;
import com.pragma.capabilities.application.dto.response.CapabilityResponse;
import com.pragma.capabilities.application.dto.response.PageResponse;
import com.pragma.capabilities.application.handler.ICapabilityHandler;
import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import com.pragma.capabilities.domain.util.OpenApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/capability")
@RequiredArgsConstructor
@Tag(name = OpenApiConstants.TITLE_CAPABILITY_REST, description = OpenApiConstants.TITLE_DESCRIPTION_CAPABILITY_REST)
public class CapabilityRestController {

    private final ICapabilityHandler capabilityHandler;

    @Operation(summary = OpenApiConstants.NEW_CAPABILITY_TITLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode =  OpenApiConstants.RESPONSE_CODE_201, description = OpenApiConstants.NEW_CAPABILITY_CREATED_MESSAGE, content = @Content),
            @ApiResponse(responseCode = OpenApiConstants.RESPONSE_CODE_400, description = OpenApiConstants.VALIDATIONS_ERRORS_MESSAGE, content = @Content)
    })
    @PostMapping("/")
    public Mono<CapabilityResponse> createCapability (@RequestBody CapabilityRequest capabilityRequest) {
        return  capabilityHandler.createCapability(capabilityRequest);
    }


    @Operation(summary = OpenApiConstants.GET_CAPABILITY_TITLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode =  OpenApiConstants.RESPONSE_CODE_200, description = OpenApiConstants.GET_CAPABILITY_MESSAGE, content = @Content),
            @ApiResponse(responseCode = OpenApiConstants.RESPONSE_CODE_404, description = OpenApiConstants.NO_DATA_MESSAGE, content = @Content)
    })
    @GetMapping("/{id}")
    public Mono<CapabilityResponse> getCapabilityById(@PathVariable Long id) {
        return capabilityHandler.getCapabilityById(id);
    }



    @Operation(summary = OpenApiConstants.GET_ALL_CAPABILITY_TITLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode =  OpenApiConstants.RESPONSE_CODE_200, description = OpenApiConstants.GET_ALL_CAPABILITY_MESSAGE,
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CapabilityResponse.class)))),
            @ApiResponse(responseCode = OpenApiConstants.RESPONSE_CODE_404, description =OpenApiConstants.NO_DATA_MESSAGE, content = @Content)
    })
    @GetMapping("/")
    public Mono<PageResponse<CapabilityResponse>> getCapabilities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(
                    description = OpenApiConstants.GET_ALL_CAPABILITY_ORDER_DESCRIPTION,
                    schema = @Schema(implementation = SortOrder.class)
            )
            @RequestParam(defaultValue = "DESC") SortOrder order,
            @Parameter(
                    description = OpenApiConstants.GET_ALL_CAPABILITY_SORT_BY_DESCRIPTION,
                    schema = @Schema(implementation = SortBy.class)
            )
            @RequestParam(defaultValue = "NAME") SortBy sortBy
    ) {
        return capabilityHandler.getCapabilities(new PageRequest(page, size, order, sortBy));
    }

    @Operation(summary = OpenApiConstants.DELETE_CAPABILITY_TITLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode =  OpenApiConstants.RESPONSE_CODE_200, description = OpenApiConstants.DELETE_CAPABILITY_MESSAGE, content = @Content),
            @ApiResponse(responseCode = OpenApiConstants.RESPONSE_CODE_404, description =  OpenApiConstants.NO_DATA_MESSAGE , content = @Content)
    })
    @DeleteMapping("/{id}")

    public Mono<Void> deleteCapabilityById(@PathVariable(value = "id")long id){

        return capabilityHandler.deleteCapabilityById( id) ;
    }




}
