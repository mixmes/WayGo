package ru.sfedu.server;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
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
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;
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

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class ServerApplication {
	@Autowired
	UserDataService dataService;

	@PersistenceContext
	EntityManager manager;

	@Autowired
	AmazonS3 client;

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

			PhotoMetaInfo metaInfo1 = new PhotoMetaInfo();
			metaInfo1.setBucketName("photo-metainfo");
			metaInfo1.setKey("myphoto");

			PhotoMetaInfo metaInfo2 = new PhotoMetaInfo();
			metaInfo2.setBucketName("photo-metainfo");
			metaInfo2.setKey("Парамоновские склады 1.jpg");

			PhotoMetaInfo metaInfo3 = new PhotoMetaInfo();
			metaInfo3.setBucketName("photo-metainfo");
			metaInfo3.setKey("Парамоновские склады 2.jpg");

			PhotoMetaInfo metaInfo4 = new PhotoMetaInfo();
			metaInfo4.setBucketName("photo-metainfo");
			metaInfo4.setKey("Парамоновские склады 3.jpg");

			PhotoMetaInfo metaInfo5 = new PhotoMetaInfo();
			metaInfo5.setBucketName("photo-metainfo");
			metaInfo5.setKey("Парамоновские склады 4.jpg");

			PhotoMetaInfo metaInfo6 = new PhotoMetaInfo();
			metaInfo6.setBucketName("photo-metainfo");
			metaInfo6.setKey("Парамоновские склады 5.jpg");

			Point point = new Point();
			point.setPointName("Парамоновские склады");
			point.setAddress(" Нижнебульварная ул.,27");
			point.setPhotos(List.of(metaInfo1,metaInfo2,metaInfo3,metaInfo4,metaInfo5,metaInfo6));
			point.setLongitude(39.72708370257682);
			point.setLatitude(47.21816711449424);
			point.setCity("Ростов-на-Дону");
			point.setDescription("Здание, построенное в 1914 году для книгоиздателя Николая Парамонова по проекту архитектора Леонида Эберга");
			Point point1 = new Point();
			point1.setPointName("Особняк Парамонова");
			point1.setAddress("Пушкинская ул., 148");
			point1.setCity("Ростов-на-Дону");
			point1.setLongitude(39.7253944369593);
			point1.setLatitude(47.22698761857818);
			point1.setDescription("Комплекс складских сооружений XIX века в Ростове-на-Дону");
			Point point2 = new Point();
			point2.setPointName("Дом братьев Мартын");
			point2.setAddress("Большая Садовая ул., 125, Ростов-на-Дону");
			point2.setCity("Ростов-на-Дону");
			point2.setLongitude(39.73626668650843);
			point2.setLatitude(47.22622431194568);
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

			dataService.save(user);
		};
	}

}
