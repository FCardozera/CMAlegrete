package com.cmalegrete;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class StressTestMembership extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080") 
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    FeederBuilder.FileBased<String> csvFeeder = csv("users.csv").circular();

    ScenarioBuilder scn = scenario("Teste de Stress com CSV")
        .feed(csvFeeder) 
        .exec(http("POST /associe")
            .post("/associe")
            .body(StringBody(session -> {
                return "{" +
                    "\"email\": \"" + session.getString("Email") + "\"," +
                    "\"name\": \"" + session.getString("Nome") + "\"," +
                    "\"cpf\": \"" + session.getString("CPF") + "\"," +
                    "\"rg\": \"" + session.getString("RG") + "\"," +
                    "\"phoneNumber\": \"" + session.getString("Telefone") + "\"," +
                    "\"address\": \"" + session.getString("Endereço") + "\"," +
                    "\"militaryOrganization\": \"" + session.getString("Organização Militar") + "\"" +
                "}";
            })).asJson()
            .check(status().is(200))
        );

    {
        setUp(
            scn.injectOpen(atOnceUsers(100))
        ).protocols(httpProtocol);
    }
}
