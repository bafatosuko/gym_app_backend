package gr.myproject.wod_core_backend.rest;

import gr.myproject.wod_core_backend.core.exceptions.*;
import gr.myproject.wod_core_backend.dto.CustomerInsertDTO;
import gr.myproject.wod_core_backend.dto.CustomerReadOnlyDTO;
import gr.myproject.wod_core_backend.service.CustomerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerService customerService;


    @Operation(
            summary = "Create a new customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Customer already exists", content = @Content)
            }
    )
    public ResponseEntity<CustomerReadOnlyDTO> saveCustomer(
            @Valid @RequestPart(name = "customer")CustomerInsertDTO customerInsertDTO,
            BindingResult bindingResult
            ) throws AppObjectAlreadyExists, AppObjectInvalidArgumentException , ValidationException, AppServerException,
            IOException {

        if( bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }

        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.saveCustomer(customerInsertDTO);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);


    }



    @Operation(
            summary = "Get paginated and sorted customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page of customers returned")
            }
    )
    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerReadOnlyDTO>> getPaginatedCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Page<CustomerReadOnlyDTO> pageResult = customerService.getPaginatedSortedCustomers(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pageResult);
    }

    @Operation(
            summary = "Get all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customers fetched")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<CustomerReadOnlyDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }


    @Operation(summary = "Delete customer by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) throws AppObjectNotFoundException {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

}
