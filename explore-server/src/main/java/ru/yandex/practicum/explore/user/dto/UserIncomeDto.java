package ru.yandex.practicum.explore.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserIncomeDto {

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @Email
    @Size(min = 6, max = 254)
    private String email;
}
