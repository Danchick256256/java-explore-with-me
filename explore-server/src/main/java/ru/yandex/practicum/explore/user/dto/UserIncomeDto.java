package ru.yandex.practicum.explore.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserIncomeDto {

    @NotBlank
    private String name;

    @Email
    private String email;
}
