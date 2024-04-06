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
import ru.sfedu.server.service.PointDataService;
import ru.sfedu.server.service.UserDataService;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class ServerApplication {
	@Autowired
	UserDataService dataService;

	@Autowired
	PointDataService pointDataService;

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

		};
	}

}
