package com.portfolio.expensetracker.portfoliomanager.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import com.portfolio.expensetracker.util.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetRequest {

    @NotNull(message = Constants.ASSET_EXTERNAL_ID_MANDATORY_MSG)
    @Pattern(regexp = Constants.ID_REGEX, message = Constants.EXTERNAL_ID_INVALID_MSG)
    private String externalId;

    @NotNull(message = Constants.ASSET_TYPE_MANDATORY_MSG)
    @Pattern(regexp = Constants.TYPE_REGEX, message = Constants.TYPE_INVALID_MSG)
    private String type;

    @NotNull(message = Constants.ASSET_ARTIFACT_INFO_MANDATORY_MSG)
    private ArtifactInformation artifactInfo;

    @NotNull(message = Constants.ASSET_PERMISSION_POLICY_MANDATORY_MSG)
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

        @NotNull(message = Constants.ASSET_GROUP_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.GROUP_ID_REGEX, message = Constants.GROUP_ID_INVALID_MSG)
        private String groupId;

        @NotNull(message = Constants.ASSET_ARTIFACT_ID_MANDATORY_MSG)
        @Pattern(regexp = Constants.ARTIFACT_ID_REGEX, message = Constants.ARTIFACT_ID_INVALID_MSG)
        private String artifactId;

        @NotNull(message = Constants.ASSET_VERSION_MANDATORY_MSG)
        @Pattern(regexp = Constants.VERSION_REGEX, message = Constants.VERSION_INVALID_MSG)
        private String version;
    }
}
