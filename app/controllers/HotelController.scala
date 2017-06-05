package controllers

import java.util.Calendar
import javax.inject._

import play.api._
import play.api.libs.json._
import play.api.mvc._
import services.{HotelDetails, HotelService, RateLimitService}

import scala.io.Source

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HotelController @Inject() (hotelService : HotelService, rateLimitService: RateLimitService) extends Controller {

  def searchHotel(apiKey: String, cityName:String, sort: Option[String]) = Action {
    implicit val hotelDetailsFormat = Json.writes[HotelDetails]
    if(!rateLimitService.checkRateLimit(apiKey, Calendar.getInstance()))
          TooManyRequests("Rate limit crossed")
    else if(sort.isEmpty)
      Ok(Json.toJson(hotelService.hotelDataForACity(cityName)).toString())
      //Ok(Json.toJson(HotelDetails(9, "eet", 10)).toString())
    else
       Ok(Json.toJson(hotelService.sortedHotelDataForACity(cityName, sort.get)).toString())

  }


}
