package $package$.api.graphql

import caliban._
import caliban.CalibanError.ValidationError
import caliban.{ CalibanError, GraphQLInterpreter, ZHttpAdapter }
import zio._
import zio.stream._
import zhttp.http._
import zio.clock._
import zio.console._
import zio.blocking._
import $package$.service.ItemService

object GraphqlRoute:
  
  private val graphiql = Http.succeed(
    Response.http(content = HttpData.fromStream(ZStream.fromResource("graphql/graphiql.html")))
  )

  def route(
      interpreter: GraphQLInterpreter[Console with Clock with Has[
        ItemService
      ], CalibanError]
    ): RHttpApp[Console with Blocking with Clock with Has[ItemService]] =
    Http.route {
      case _ -> Root / "api" / "graphql" => ZHttpAdapter.makeHttpService(interpreter)
      $if(add_websocket_endpoint.truthy)$case _ -> Root / "ws" / "graphql"  => ZHttpAdapter.makeWebSocketService(interpreter)$endif$
      case _ -> Root / "graphiql"        => graphiql
    }