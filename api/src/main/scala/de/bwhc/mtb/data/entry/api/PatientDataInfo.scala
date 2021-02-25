package de.bwhc.mtb.data.entry.api


import java.time.YearMonth


import play.api.libs.json.Json

import de.bwhc.util.json._
import de.bwhc.util.json.time._

import de.bwhc.mtb.data.entry.dtos.{
  Patient,
  Gender,
}

import de.bwhc.mtb.data.entry.views.{
  Or,
  NotAvailable
}


final case class PatientDataInfo
(
  id: Patient.Id,
  gender: String,
  birthDate: NotAvailable Or YearMonth,
  numberOfIssues: Int
)

object PatientDataInfo
{
  implicit val format = Json.writes[PatientDataInfo]
}


