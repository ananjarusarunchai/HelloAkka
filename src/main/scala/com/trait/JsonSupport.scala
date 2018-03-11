package com.`trait`

import akka.http.scaladsl.marshallers.sprayjson._
import spray.json._
import com.model.Item


trait JsonSupport  extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)

}
