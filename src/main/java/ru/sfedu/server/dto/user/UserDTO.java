package ru.sfedu.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sfedu.server.dto.point.PointCheckInDTO;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.dto.route.RouteCheckInDTO;
import ru.sfedu.server.dto.route.RouteDTO;
import ru.sfedu.server.dto.route.RouteGradeDTO;
import ru.sfedu.server.dto.subscription.SubscriptionTransactionDTO;
import ru.sfedu.server.model.route.RouteGrade;
import ru.sfedu.server.model.subscription.Subscription;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String uid;
    private String email;
    private String name;
    private Set<RouteGradeDTO> routeGrades;
    private Set<RouteCheckInDTO> routeCheckIns;
    private Set<PointCheckInDTO> pointCheckIns;
//    private Set<SubscriptionTransactionDTO> subscriptionTransactions;
//    private Subscription subscription;
    private Set<RouteDTO> favouriteRoutes;
    private Set<PointDTO> favouritePoints;
}
