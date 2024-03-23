package ru.sfedu.server.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteCheckInDTO {
    private long id;
    private Date date;
}
