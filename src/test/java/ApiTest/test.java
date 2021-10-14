package ApiTest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.restassured.authentication.OAuthSignature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;


public class test {

    String idBoard;
    String idList;
    String idCart1;
    String idCart2;
    String key = "bf590fe10c813604b52ef2d7a1b7a2a3";
    String token = "ebf0668dfe7eb2185753182cec297ed166f833c87248357ad209b405b601bb74";



    @BeforeClass
    public void beforeClass() {

        baseURI = ConfigurationReader.get("TrelloUrl");

    }

    @Test
    public void createBoard() {


        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .when()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", "DenemeBoard")
                .and().post("/boards");
        System.out.println(response);

        assertEquals(response.statusCode(), 200);
        assertEquals(response.contentType(), "application/json; charset=utf-8");


    }
    @Test(priority = 2)
    public  void  createCards(){

        Response response = given().log().all().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("name","newList")
                .queryParam("idBoard",idBoard)
                .when().post("1/lists");
        response.prettyPrint();

        System.out.println("New List Created");

        idList=response.path("id");

        response = given().log().all().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("idList",idList)
                .when().post("1/cards/");
        response.prettyPrint();
        idCart1=response.path("id");
        assertEquals(response.statusCode(),200);

        System.out.println("New Card Created");

        response = given().log().all().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("idList",idList)
                .when().post("1/cards/");
        response.prettyPrint();
        idCart2=response.path("id");
        assertEquals(response.statusCode(),200);

        System.out.println("New Card Created");

    }
    @Test(priority = 3)
    public  void  updateCard(){

        Response response = given().log().all().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("id",idCart1)
                .queryParam("name","UpdatedCardName")
                .and().pathParam("id",idCart1)
                .when().put("1/cards/{id}");
        response.prettyPrint();

        assertEquals(response.statusCode(),200);
        System.out.println("Card Updated");

    }

    @Test(priority = 4)
    public  void  deleteCards(){

        Response response=   given()
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("id",idCart1)
                .and().pathParam("id",idCart1)
                .when().delete("1/cards/{id}");
        assertEquals(response.statusCode(),200);

        given()
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("id",idCart2)
                .and().pathParam("id",idCart2)
                .when().delete("1/cards/{id}");
        assertEquals(response.statusCode(),200);
        System.out.println("Cards Deleted");

    }
    @Test(priority = 5)
    public void deleteBoard(){

        Response response = given()
                .queryParam("key",key)
                .queryParam("token",token)
                .queryParam("id",idBoard)
                .and().pathParam("id",idBoard)
                .when().delete("1/boards/{id}");
        assertEquals(response.statusCode(),200);

        System.out.println("Board Deleted");

    }
}





