package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
