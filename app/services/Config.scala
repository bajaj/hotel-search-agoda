package services

import com.google.inject.Singleton
import entities.ApiRequestDetails

import scala.collection.concurrent.TrieMap

/**
  * Created by bajaj on 04/06/17.
  */

@Singleton
class Config {
  val defaultMaxNumberOfRequestPerTenSeconds = 10
  val keyValueConfig: TrieMap[String, Int] = new TrieMap()

  def getRequestPerTenSecondsForApiKey(apiKey : String) : Int = keyValueConfig.getOrElse(apiKey, defaultMaxNumberOfRequestPerTenSeconds)

  def setRequestForTheApiKey(apiKey: String, noOfRequest:Int) = keyValueConfig.put(apiKey, noOfRequest)

}
