package com.gestion.stock.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for permission modification operations
 * Returns the updated user information with modified permissions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionModificationResponseDTO {

    private Long userId;

    private String username;

    private String permissionName;

    private Boolean granted;

    private List<String> effectivePermissions;

    private String message;
}

