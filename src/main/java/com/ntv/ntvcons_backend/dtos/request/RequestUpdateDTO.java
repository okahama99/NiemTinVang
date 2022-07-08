package com.ntv.ntvcons_backend.dtos.request;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
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
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long requestId;

    @Positive
    @NotNull(message = "projectId REQUIRED for update")
    private Long projectId;

    @Positive
    @NotNull(message = "requestTypeId REQUIRED for update")
    private Long requestTypeId;

    @Positive
    @NotNull(message = "RequesterId REQUIRED for update")
    private Long RequesterId;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "requestDate REQUIRED for update")
    private LocalDateTime requestDate;

    @ApiModelProperty(example = "Yêu cầu chi tiêu xxx") /* Hint for Swagger */
    @Size(max = 100, message = "requestDesc max length: 100 characters")
    @NotNull(message = "requestDesc REQUIRED for update")
    private String requestDesc;

    private List<RequestDetailUpdateDTO> requestDetailList;

    @Positive
    private Long verifierId;

    private Boolean isVerified;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private LocalDateTime verifyDate;

    @ApiModelProperty(example = "Chi tiêu hợp lý, đồng ý") /* Hint for Swagger */
    @Size(max = 100, message = "verifyNote max length: 100 characters")
    private String verifyNote;

    private Boolean isApproved;
}
