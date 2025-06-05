package system.dev.marques.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.dev.marques.domain.dto.responses.UserAdminResponse;
import system.dev.marques.service.AdminService;

@RestController
@RequestMapping("/home/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Operations available only for administrators")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/list")
    @Operation(summary = "List users", description = "Retrieve a paginated list of all users in the system.",
            tags = {"Admin"})
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAdminResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions (Admin role required)",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User do not have permission")))
    public ResponseEntity<Page<UserAdminResponse>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(adminService.findAll(pageable));
    }

    @GetMapping("/list/{id}")
    @Operation(summary = "List user by id", description = "Retrieve the found user.",
            tags = {"Admin"})
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAdminResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions (Admin role required)",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User do not have permission")))
    @ApiResponse(responseCode = "400", description = "Bad Request - user not found",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User not found by id")))
    public ResponseEntity<UserAdminResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.findById(id));
    }

    @DeleteMapping("/list/user/{id}")
    @Operation(summary = "Delete user by id", description = "Remove the user.",
            tags = {"Admin"})
    @ApiResponse(responseCode = "204", description = "User removed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions (Admin role required)",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User do not have permission")))
    @ApiResponse(responseCode = "400", description = "Bad Request - user not found",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User not found by id")))
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
