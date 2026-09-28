package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotNull
    private Long roleId;

    private String roleName;
    private boolean enabled;
    private long productCount;
    private LocalDateTime createdAt;
}
