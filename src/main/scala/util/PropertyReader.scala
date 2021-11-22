package util

import java.io.File
import java.util.Properties
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.io.Source
import com.typesafe.config.{ Config, ConfigFactory }
import lombok.Generated

object PropertyReader {

  def readProperties(path: String): mutable.Map[String, String] = {
    val filePath = new File(getClass.getResource("/application.conf").getPath)
    val props = new Properties()
    props.load(Source.fromFile(filePath).reader())
    props.asScala
  }
}