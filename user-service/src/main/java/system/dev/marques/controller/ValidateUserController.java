package system.dev.marques.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("validate")
@Tag(name = "Activation", description = "Operations for account validation/activation")
public class ValidateUserController {

    private final UserService userService;

    @PostMapping("/google")
    @Operation(summary = "Enable user", description = "Retrieve Enabled User.",
            tags = {"Activation"})
    @ApiResponse(responseCode = "201", description = "User created via Google enabled successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "410", description = "Gone - Token is no longer valid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Token is invalid or expired")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User have invalid fields")))
    public ResponseEntity<UserEnabledResponse> enableUserGoogle(@RequestParam String token,
                                                                @RequestBody @Valid UserRequestGoogle requestGoogle
            , Principal principal) {
        return new ResponseEntity<>(userService.enableUserFromGoogle(token, requestGoogle, principal),
                HttpStatus.CREATED);
    }

    @PostMapping("/form-login")
    @Operation(summary = "Enable user", description = "Retrieve Enabled User.",
            tags = {"Activation"})
    @ApiResponse(responseCode = "201", description = "User created via Form enabled successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "410", description = "Gone - Token is no longer valid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Token is invalid or expired")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User have invalid fields")))
    public ResponseEntity<UserEnabledResponse> enableUserForm(@RequestParam String token,
                                                              @RequestBody @Valid UserEnableRequest request
            , Principal principal) {
        return new ResponseEntity<>(userService.enableUser(token, request, principal), HttpStatus.CREATED);
    }

}
