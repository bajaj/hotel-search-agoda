package services

import com.google.inject.Singleton
import entities.ApiRequestDetails

import scala.collection.concurrent.TrieMap

/**
  * Created by bajaj on 04/06/17.
  */

trait ConfigService{
  def getKeyValueOrDefault(key: String, defaultValue: Int) : Int
  def setKeyValuePair(key: String, value: Int)
  def getKeyValue(key: String) : Option[Int]
}

@Singleton
class ConfigImpl extends ConfigService {
  val keyValueConfig: TrieMap[String, Int] = new TrieMap()

  def getKeyValueOrDefault(key: String, defaultValue: Int) : Int = keyValueConfig.getOrElse(key, defaultValue)

  def setKeyValuePair(key: String, value: Int) = keyValueConfig.put(key, value)

  def getKeyValue(key: String) : Option[Int] = keyValueConfig.get(key)

}
