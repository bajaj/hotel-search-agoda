package services

import play.api.libs.json.{JsValue, Json, Writes}

/**
  * Created by bajaj on 04/06/17.
  */
case class HotelDetails(hotelId: Int, room: String, price: Int) {
  //implicit val HotelDetailsReads = Json.reads[HotelDetails]
  //implicit val hotelDetailsFormat = Json.writes[HotelDetails]


//  implicit val implicitHotelDetailsWrites = new Writes[HotelDetails] {
//    def writes(hotelDetails: HotelDetails): JsValue = {
//      Json.obj(
//        "id" -> hotelDetails.hotelId,
//        "room" -> hotelDetails.room,
//        "price" -> hotelDetails.price
//
//      )
//    }
//  }

}
