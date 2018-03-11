package com.web.app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directive1
import akka.stream.ActorMaterializer
import com.`trait`.JsonSupport
import com.model.Item

import scala.io.StdIn

object WebServer extends App with JsonSupport {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val execlyutionContext = system.dispatcher
    val textParam: Directive1[String] = parameter("text".as[String])
    val lengthDirective: Directive1[Int] = textParam.map(text => text.length)



    val route =
      path("hello") {
        get {
          complete(
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>")
          )
        }
      }~
      path("hello2"){
        get{
          lengthDirective { lengtTxt => complete(lengtTxt.toString)}
          extractMatchedPath { matched => complete(matched.toString)}
          extractMatchedPath {
            matched => complete(Item(42, "Name1"))
          }
        }
      }~
      path("addItem") {
        post {
          entity(as[Item]) { item =>
            val itemId = item.id
            val itemName = item.name
            complete(s"Add a new user => $itemId , $itemName")
          }
        }
      }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done





}


