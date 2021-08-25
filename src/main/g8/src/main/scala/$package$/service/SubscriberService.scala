package com.example.service

import $package$.domain.ItemId
import zio.*
import zio.stream.*

trait SubscriberService:
  def publishDeleteEvents(deletedItemId: ItemId): IO[Nothing, List[Boolean]]
  def showDeleteEvents: Stream[Nothing, ItemId]
  def getQueue: UIO[List[Queue[ItemId]]]
  
object SubscriberService {
  def publishDeleteEvents(deletedItemId: ItemId): ZIO[Has[SubscriberService], Nothing, List[Boolean]] =
    ZIO.serviceWith[SubscriberService](_.publishDeleteEvents(deletedItemId))

  def showDeleteEvents: ZStream[Has[SubscriberService], Nothing, ItemId] =
    ZStream.accessStream(_.get.showDeleteEvents)

  def getQueue: ZIO[Has[SubscriberService], Nothing, List[Queue[ItemId]]] =
    ZIO.serviceWith[SubscriberService](_.getQueue)
}