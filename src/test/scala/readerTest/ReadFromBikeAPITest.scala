package readerTest

import org.scalatest.funsuite.AnyFunSuite
import org.apache.spark.sql.SparkSession
import java.io.File

import readers.ReadFromBikeAPI
import util.{ PropertyReader }

class ReadFromBikeAPITest extends AnyFunSuite {
  val spark = SparkSession.builder()
    .config("spark.sql.orc.impl", "native")
    .master("local[*]")
    .enableHiveSupport()
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")
  val fileName = "/application.conf"
  val props = PropertyReader.readProperties(fileName)

  val builderObj = new util.UrlBuilder()
  val apiUrl = builderObj.buildUrl(props)

  test("ReadFromBikeAPI.execute") {
    ReadFromBikeAPI.execute(spark, props, apiUrl.toString())

    val tableName = props.get("TABLE_NAME").get
    val result = spark.sql("select count(1) from " + tableName).head().getLong(0)
    assert(result > 0)
  }

}