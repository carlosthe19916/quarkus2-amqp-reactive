package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.utils.ArtemisServer;
import org.acme.utils.PostgreSQLServer;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(ArtemisServer.class)
@QuarkusTestResource(PostgreSQLServer.class)
@TestHTTPEndpoint(ReactiveTemperatureResource.class)
public class ReactiveTemperatureResourceTest {

    @Test
    public void testEndpoint() {
        given()
                .when().post("/")
                .then()
                .statusCode(204);
    }

}
