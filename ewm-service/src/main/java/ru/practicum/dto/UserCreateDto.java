package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @Size(min = 2, max = 250, message = "Name must be from 2 to 250 characters long.")
    @NotBlank(message = "Name may not be blank.")
    private String name;

    @Email(message = "Incorrect email pattern.")
    @Size(min = 6, max = 320, message = "Email must be from 6 to 320 characters long.")
    @NotBlank(message = "Email may not be blank.")
    private String email;

}