package unit

import org.scalatest.funsuite.AnyFunSuite
import org.apache.spark.sql.SparkSession
import org.json4s.jackson.JsonMethods.parse
import scala.io.Source.fromURL
import java.io.File

import model.{ Result }

import writers.WriteToHive

class WriteToHiveTest extends AnyFunSuite {
  val spark = SparkSession.builder()
    .config("spark.sql.orc.impl", "native")
    .master("local[*]")
    .enableHiveSupport()
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")
  val testHiveTableName = "bikes_test"
  val hiveWriter = new WriteToHive()

  implicit val formats = org.json4s.DefaultFormats
  val apiUrl = new File(getClass.getResource("/testBikeData.json").getPath).toURI().toURL()
  val apiUrlResponse = parse(fromURL(apiUrl).mkString)
    .extract[Result]

  test("UrlBuilder.buildUrl") {
    import spark.implicits._
    val sourceDataset = spark.createDataset(apiUrlResponse.bikes)
    hiveWriter.writeDataFrame(spark, sourceDataset, testHiveTableName)

    val result = spark.sql("select count(1) from " + testHiveTableName).head().getLong(0)
    assert(result > 0)
  }
}