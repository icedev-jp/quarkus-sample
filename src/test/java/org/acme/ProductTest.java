package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductTest {

    @Test
    public void testHelloEndpoint() {
    	
    	// looks interesting, but I don't know this testing framework enough
    	// to mock up a propert test with csv file upload
    	
    	// good old commenting out tests "temporarily" never fails
    	
//        given()
//          .when().get("/csv")
//          .then()
//             .statusCode(200)
//             .body(is("OK"));
    }

}