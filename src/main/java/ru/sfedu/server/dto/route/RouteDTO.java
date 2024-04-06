package ru.sfedu.server.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sfedu.server.dto.point.PointDTO;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    private long id;
    private long length;
    private String city;
    private String description;
    private String routeName;
    private Set<RouteGradeDTO> routeGrades;
    private List<PointDTO> stopsOnRoute;
}
