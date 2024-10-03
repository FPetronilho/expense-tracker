package com.portfolio.expensetracker.dto.portfoliomanager.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetResponse {

    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String externalId;
    private String type;
    private ArtifactInformation artifactInfo;
    private PermissionPolicy permissionPolicy;

    @ToString
    @Getter
    @RequiredArgsConstructor
    public enum PermissionPolicy {

        OWNER("owner"), // digital user owns this asset
        VIEWER("viewer"); // digital user can only view this asset

        private final String value;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ArtifactInformation {

        private String groupId;
        private String artifactId;
        private String version;
    }
}
