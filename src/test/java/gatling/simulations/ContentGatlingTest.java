package gatling.simulations;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.headerRegex;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Performance test for the Content entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v8.7.3/generators/gatling#logging-tips">Logging tips</a>
 */
public class ContentGatlingTest extends Simulation {

    String baseURL = Optional.ofNullable(System.getProperty("baseURL")).orElse("http://localhost:8080");

    HttpProtocolBuilder httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources(); // Silence all resources like css or css so they don't clutter the results

    Map<String, String> headersHttp = Map.of("Accept", "application/json");

    Map<String, String> headersHttpAuthentication = Map.of("Content-Type", "application/json", "Accept", "application/json");

    Map<String, String> headersHttpAuthenticated = Map.of("Accept", "application/json", "Authorization", "${access_token}");

    ChainBuilder scn = exec(http("First unauthenticated request").get("/api/account").headers(headersHttp).check(status().is(401)))
        .exitHereIfFailed()
        .pause(10)
        .exec(
            http("Authentication")
                .post("/api/authenticate")
                .headers(headersHttpAuthentication)
                .body(StringBody("{\"username\":\"admin\", \"password\":\"admin\"}"))
                .asJson()
                .check(header("Authorization").saveAs("access_token"))
        )
        .exitHereIfFailed()
        .pause(2)
        .exec(http("Authenticated request").get("/api/account").headers(headersHttpAuthenticated).check(status().is(200)))
        .pause(10)
        .repeat(2)
        .on(
            exec(http("Get all contents").get("/api/contents").headers(headersHttpAuthenticated).check(status().is(200)))
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new content")
                        .post("/api/contents")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                "\"title\": \"SAMPLE_TEXT\"" +
                                ", \"uri\": \"SAMPLE_TEXT\"" +
                                ", \"description\": null" +
                                ", \"path\": \"SAMPLE_TEXT\"" +
                                ", \"source\": \"SAMPLE_TEXT\"" +
                                ", \"params\": \"SAMPLE_TEXT\"" +
                                ", \"bodyText\": null" +
                                ", \"textSize\": 0" +
                                ", \"structuredContent\": null" +
                                ", \"structureSize\": 0" +
                                ", \"author\": \"SAMPLE_TEXT\"" +
                                ", \"language\": \"SAMPLE_TEXT\"" +
                                ", \"type\": \"SAMPLE_TEXT\"" +
                                ", \"subtype\": \"SAMPLE_TEXT\"" +
                                ", \"mineType\": \"SAMPLE_TEXT\"" +
                                ", \"publishDate\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"offensiveFlag\": \"SAMPLE_TEXT\"" +
                                ", \"favicon\": \"SAMPLE_TEXT\"" +
                                ", \"dateCreated\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"lastUpdate\": \"2020-01-01T00:00:00.000Z\"" +
                                "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_content_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(exec(http("Get created content").get("${new_content_url}").headers(headersHttpAuthenticated)).pause(10))
                .exec(http("Delete created content").delete("${new_content_url}").headers(headersHttpAuthenticated))
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the Content entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
