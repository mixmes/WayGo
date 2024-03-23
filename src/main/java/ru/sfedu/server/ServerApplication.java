package ru.sfedu.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.point.PointCheckIn;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.model.route.RouteCheckIn;
import ru.sfedu.server.model.route.RouteGrade;
import ru.sfedu.server.model.subscription.Subscription;
import ru.sfedu.server.model.subscription.SubscriptionPlan;
import ru.sfedu.server.model.subscription.SubscriptionTransaction;
import ru.sfedu.server.model.user.User;
import ru.sfedu.server.service.UserDataService;

import java.util.Date;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class ServerApplication {
	@Autowired
	UserDataService dataService;

	@PersistenceContext
	EntityManager manager;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return (args) -> {
			User user = new User();
			user.setName("Иван Петухов");
			user.setEmail("petuhof@gmail.ru");

			User user1 = new User();
			user1.setName("Глаша Коровкина");
			user1.setEmail("glasha@gmail.ru");

			Point point = new Point();
			point.setPointName("Парамоновские склады");
			point.setAddress(" Нижнебульварная ул.,27");
			point.setLongitude(47.21816711449424);
			point.setLatitude(39.72708370257682);
			point.setCity("Ростов-на-Дону");
			point.setDescription("Здание, построенное в 1914 году для книгоиздателя Николая Парамонова по проекту архитектора Леонида Эберга");
			Point point1 = new Point();
			point1.setPointName("Особняк Парамонова");
			point1.setAddress("Пушкинская ул., 148");
			point1.setCity("Ростов-на-Дону");
			point1.setLongitude(47.22698761857818);
			point1.setLatitude(39.7253944369593);
			point1.setDescription("Комплекс складских сооружений XIX века в Ростове-на-Дону");
			Point point2 = new Point();
			point2.setPointName("Дом братьев Мартын");
			point2.setCity("Ростов-на-Дону");
			point2.setLongitude(447.22622431194568);
			point2.setLatitude(39.73626668650843);
			point2.setDescription("Здание в Ростове-на-Дону, расположенное на пересечении Большой Садовой улицы и Крепостного переулка.");


			PointCheckIn pointCheckIn = new PointCheckIn();
			pointCheckIn.setDate(new Date());
			pointCheckIn.setPoint(point);

			RouteCheckIn checkIn = new RouteCheckIn();
			checkIn.setDate(new Date());


			Route route = new Route();
			route.setRouteName("Маршрут 1");
			route.setLength(12);
			route.setCity("Ростов-на-Дону");
			route.setStopsOnRoute(Set.of(point1,point,point2));
			checkIn.setRoute(route);
			route.setDescription("Ростов по-отечески принимает всех гостей, так что давайте любоваться им вместе с лучших ракурсов! ");

			Route route1 = new Route();
			route1.setRouteName("Маршрут 2");

			user.setFavouriteRoutes(Set.of(route));
			user.setFavouritePoints(Set.of(point));

			user1.setFavouritePoints(Set.of(point1));
			user1.setFavouriteRoutes(Set.of(route));

			RouteGrade routeGrade = new RouteGrade();
			routeGrade.setGrade(5);
			routeGrade.setRoute(route);

			RouteGrade routeGrade1 = new RouteGrade();
			routeGrade1.setGrade(2);
			routeGrade.setRoute(route);

			user.setRouteGrades(Set.of(routeGrade));
			user.setPointCheckIns(Set.of(pointCheckIn));
			user.setRouteCheckIns(Set.of(checkIn));

			user1.setRouteGrades(Set.of(routeGrade1));
			user1.setPointCheckIns(Set.of(pointCheckIn));
			user1.setRouteCheckIns(Set.of(checkIn));


//			dataService.save(user);
		};
	}

}
