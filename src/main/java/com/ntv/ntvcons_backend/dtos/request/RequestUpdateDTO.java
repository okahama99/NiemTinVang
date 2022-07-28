package com.ntv.ntvcons_backend.dtos.request;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateDTO extends BaseUpdateDTO {
    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long requestId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for Update")
    private Long projectId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "requestTypeId REQUIRED for Update")
    private Long requestTypeId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "RequesterId REQUIRED for Update")
    private Long RequesterId;

    @ApiModelProperty(example = "Yêu cầu xxx",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "requestName max length: 100 characters")
    @NotNull(message = "requestName REQUIRED for Update")
    private String requestName;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm",
            notes = "NOT NULL; date <= now") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "requestDate REQUIRED for Update")
    private String requestDate;

    @ApiModelProperty(example = "Yêu cầu chi tiêu xxx",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "requestDesc max length: 100 characters")
    @NotNull(message = "requestDesc REQUIRED for Update")
    private String requestDesc;

    @ApiModelProperty(notes = "Nullable; size >= 1 (if not null)")
    @Size(min = 1, message = "Needed at least 1 detail")
    private List<RequestDetailUpdateDTO> requestDetailList;

    /* Verifying */
    @ApiModelProperty(notes = "Nullable; Id > 0 (if not null)")
    @Positive
    private Long verifierId;

    @ApiModelProperty(notes = "Nullable")
    private Boolean isVerified;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm",
            notes = "Nullable; date <= now (if not null)") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String verifyDate;

    @ApiModelProperty(example = "Chi tiêu hợp lý, đồng ý",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "verifyNote max length: 100 characters")
    private String verifyNote;

    @ApiModelProperty(notes = "Nullable")
    private Boolean isApproved;
}
