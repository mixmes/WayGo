package ru.sfedu.server.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteGradeDTO {
    private long id;
    private int grade;
}
