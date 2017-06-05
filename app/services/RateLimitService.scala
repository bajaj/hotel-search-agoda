package services

import java.util.Calendar
import java.util.concurrent.TimeUnit

import scala.collection.concurrent.TrieMap
import javax.inject.{Inject, Singleton}

import entities.ApiRequestDetails



/**
  * Created by bajaj on 04/06/17.
  */

trait RateLimitService {
  def checkRateLimit(apiKey: String, apiRequestTime: Calendar) : Boolean
}


@Singleton
class RateLimit @Inject()(config: Config) extends RateLimitService {
  val apiKeyMap: TrieMap[String, ApiRequestDetails] = new TrieMap()
  val rateLimitMilliSeconds = 10 * 1000
  val suspendedTimeLimitInMilliSeconds  = 1 * 60 * 1000

  override def checkRateLimit(apiKey: String, apiRequestTime: Calendar) : Boolean = {
    val apiRequestDetails = apiKeyMap.getOrElseUpdate(apiKey, new ApiRequestDetails(apiKey))

    if( (!apiRequestDetails.suspended || suspendedTimeOver(apiRequestDetails, apiRequestTime))
      && requestWithinRateLimit(apiRequestDetails, apiRequestTime)) {
      apiRequestDetails.addRequest(apiRequestTime.getTimeInMillis, TimeUnit.MILLISECONDS)
      apiRequestDetails.removeRequestTimeStampBeforeGiveTimeStamp(apiRequestTime.getTimeInMillis - rateLimitMilliSeconds, TimeUnit.MILLISECONDS)

      if(apiRequestDetails.suspended)
        apiRequestDetails.suspended = false

      updateApiRateDetails(apiRequestDetails)
      return true
    }

    if(!apiRequestDetails.suspended) {
      apiRequestDetails.suspended = true
      apiRequestDetails.suspendedTimestamp = Calendar.getInstance()
      updateApiRateDetails(apiRequestDetails)
      return false
    }

    false
  }

  def updateApiRateDetails(apiRequestDetails: ApiRequestDetails) = apiKeyMap.put(apiRequestDetails.apiKey, apiRequestDetails)

  def suspendedTimeOver(apiRequestDetails: ApiRequestDetails, apiRequestTime: Calendar): Boolean = {
    (apiRequestTime.getTimeInMillis - apiRequestDetails.suspendedTimestamp.getTimeInMillis) >= suspendedTimeLimitInMilliSeconds
  }

  def requestWithinRateLimit(apiRequestDetails: ApiRequestDetails, apiRequestTime: Calendar): Boolean = {
    val noOfRequest = config.getRequestPerTenSecondsForApiKey(apiRequestDetails.apiKey)

    if(apiRequestDetails.requestTimeStampInMilliSecondsList.size < noOfRequest)
      return true

    if((apiRequestTime.getTimeInMillis - apiRequestDetails.requestTimeStampInMilliSecondsList.head) >= rateLimitMilliSeconds)
      return true

    false
  }




}
