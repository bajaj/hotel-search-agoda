package entities

import java.util.Calendar
import java.util.concurrent.TimeUnit

import scala.collection.mutable.ListBuffer

/**
  * Created by bajaj on 04/06/17.
  */
class ApiRequestDetails(val apiKey: String) {
  var requestTimeStampInMilliSecondsList = new ListBuffer[Long]()
  var suspended = false
  var suspendedTimestamp = Calendar.getInstance()

  def addRequest(requestTimeStamp: Long, timeUnit: TimeUnit) = {
    requestTimeStampInMilliSecondsList += timeUnit.toMillis(requestTimeStamp)
  }

  def removeRequestTimeStampBeforeGiveTimeStamp(requestTimeStamp: Long, timeUnit: TimeUnit) = {
    while(requestTimeStampInMilliSecondsList.nonEmpty && requestTimeStampInMilliSecondsList.head < timeUnit.toMillis(requestTimeStamp))
      requestTimeStampInMilliSecondsList.remove(0)
  }

}
