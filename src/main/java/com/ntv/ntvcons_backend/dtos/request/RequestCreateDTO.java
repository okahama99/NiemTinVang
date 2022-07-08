package com.ntv.ntvcons_backend.dtos.request;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
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
public class RequestCreateDTO extends BaseCreateDTO {
    @Positive
    @NotNull(message = "projectId REQUIRED for create")
    private Long projectId;

    @Positive
    @NotNull(message = "requestTypeId REQUIRED for create")
    private Long requestTypeId;

    @Positive
    @NotNull(message = "RequesterId REQUIRED for create")
    private Long RequesterId;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "requestDate REQUIRED for create")
    private LocalDateTime requestDate;

    @ApiModelProperty(example = "Yêu cầu chi tiêu xxx") /* Hint for Swagger */
    @Size(max = 100, message = "requestDesc max length: 100 characters")
    @NotNull(message = "requestDesc REQUIRED for create")
    private String requestDesc;

    @Size(min = 1, message = "Needed at least 1 detail")
    @NotNull(message = "requestDetailList REQUIRED for create")
    private List<RequestDetailCreateDTO> requestDetailList;
}
