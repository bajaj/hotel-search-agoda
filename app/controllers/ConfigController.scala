package controllers
import java.util.Calendar

import play.api.mvc._
import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import services.{ConfigService, HotelDetails}

/**
  * Created by bajaj on 06/06/17.
  */
@Singleton
class ConfigController @Inject() (configService: ConfigService)  extends Controller {

  def setGetConfig(key: String, value: Option[Int]) = Action {
    if (value.isEmpty) {
      val configValue = configService.getKeyValue(key)

      if (configValue.isEmpty)
        Ok("value does not exists for this key")
      else
        Ok(configValue.get.toString)
    }
    else {
      configService.setKeyValuePair(key, value.get)
      Ok("value update for the key")
    }
  }

}
