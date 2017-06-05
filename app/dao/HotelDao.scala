package dao

import javax.inject.Singleton

import scala.io.Source

/**
  * Created by bajaj on 04/06/17.
  */


trait HotelDao {
  def getHotelsData : List[HotelEntity]
}

@Singleton
class HotelDaoImpl extends HotelDao {

  def getHotelsData : List[HotelEntity] = {
    println("reading hotels data")
    val bufferedSource = Source.fromFile("/Users/bajaj/Downloads/play-scala-starter-example/hoteldb.csv")
    val hotelEntites = bufferedSource.getLines().drop(1).
      map(line => line.split(",").map(_.trim)).map(col => csvLineToHotel(col)).toList
    bufferedSource.close
    hotelEntites
  }

  def csvLineToHotel(col : Array[String]) : HotelEntity = new HotelEntity(col(0), col(1).toInt, col(2), col(3).toInt)

}
