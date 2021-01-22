package de.bwhc.mtb.data.entry.api


import java.time.Instant

import scala.util.Either

import scala.concurrent.{
  ExecutionContext,
  Future
}

import cats.data.NonEmptyList

import de.bwhc.util.ddd.Event
import de.bwhc.util.spi._

import de.bwhc.mtb.data.entry.dtos.{
  Patient,
  MTBFile
}

import de.bwhc.mtb.data.entry.views.MTBFileView


trait MTBDataServiceProvider extends SPI[MTBDataService]


trait MTBDataService
{

  def process(
    cmd: MTBDataService.Command
  )(
    implicit ec: ExecutionContext
  ): Future[Either[MTBDataService.Error,MTBDataService.Response]]

  def !(cmd: MTBDataService.Command)(implicit ec: ExecutionContext) = process(cmd)


  def patientsWithIncompleteData(
    implicit ec: ExecutionContext
  ): Future[Iterable[Patient]]


//  @deprecated
  def mtbfile(
    patient: Patient.Id
  )(
    implicit ec: ExecutionContext
  ): Future[Option[MTBFile]]


  def mtbfileView(
    patient: Patient.Id
  )(
    implicit ec: ExecutionContext
  ): Future[Option[MTBFileView]]


  def dataQualityReport(
    patient: Patient.Id
  )(
    implicit ec: ExecutionContext
  ): Future[Option[DataQualityReport]]

}


object MTBDataService extends SPILoader(classOf[MTBDataServiceProvider])
{

  sealed abstract class Command
  object Command
  {
    final case class Upload(mtbfile: MTBFile) extends Command

    final case class Delete(patient: Patient.Id) extends Command
  }

  sealed abstract class Response extends Event
  object Response
  {

    final case class Imported
    (
      result: MTBFile,
      timestamp: Instant = Instant.now
    ) extends Response
  
    final case class IssuesDetected
    (
      result: DataQualityReport,
      timestamp: Instant = Instant.now
    ) extends Response
  
    final case class Deleted(
      patient: Patient.Id,
      timestamp: Instant = Instant.now
    ) extends Response

  }

  sealed abstract class Error
  object Error
  {
    final case class InvalidData(qc: DataQualityReport) extends Error

    final case class UnspecificError(msg: String) extends Error
  }



}
