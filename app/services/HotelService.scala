package services

import javax.inject.{Inject, Singleton}

import dao.{HotelDao, HotelEntity}

/**
  * Created by bajaj on 04/06/17.
  */


trait HotelService{
  def hotelDataForACity(cityName: String) : List[HotelDetails]
  def sortedHotelDataForACity(cityName: String, sortBy: String) : List[HotelDetails]
}


@Singleton
class Hotel @Inject() (hotelDao : HotelDao) extends HotelService {
  val hotelEntities = hotelDao.getHotelsData
  val cityNames = hotelEntities.map(h=>h.cityName).toSet
  val cityHotelMap =  scala.collection.mutable.Map[String, List[HotelEntity]]()
  loadCityHotelMap()

  override def hotelDataForACity(cityName: String) : List[HotelDetails] = {
    cityHotelMap.getOrElse(cityName, List[HotelEntity]()).map(hotelEntityToHotelDetails)
  }

  def sortedHotelDataForACity(cityName: String, sortBy: String) : List[HotelDetails] = {
    cityHotelMap.getOrElse(cityName, List[HotelEntity]())
                .map(hotelEntityToHotelDetails)
                .sortWith(getSortingFunction(sortBy))
  }

  def getSortingFunction(sortBy: String) = {
    if(sortBy == "ASC")
      (x:HotelDetails, y:HotelDetails) => x.price < y.price
    else if (sortBy == "DESC")
      (x:HotelDetails, y:HotelDetails) => x.price < y.price
    else
      (x:HotelDetails, y:HotelDetails) => true
  }

  def hotelEntityToHotelDetails(he: HotelEntity) : HotelDetails =
     HotelDetails(he.hotelId, he.room, he.price)

  def loadCityHotelMap() = hotelEntities.foreach(he => {
    val newList = cityHotelMap.getOrElse(he.cityName, List[HotelEntity]())
    cityHotelMap.put(he.cityName, he::newList)
  })
}
