package unit

import org.scalatest.funsuite.AnyFunSuite
import org.apache.spark.sql.SparkSession
import java.io.File

import util.{ PropertyReader, UrlBuilder }

class UrlBuilderTest extends AnyFunSuite {
  val spark = SparkSession.builder()
    .config("spark.sql.orc.impl", "native")
    .master("local[*]")
    .enableHiveSupport()
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")
  val fileName = "/application.conf"
  val summaryUrl = "https://bikeindex.org:443/api/v3/search"
  val detailUrl = "https://bikeindex.org:443/api/v3/search?page=141255&per_page=5&stolenness=all&access_token=incidents"

  test("UrlBuilder.buildUrl") {
    val props = PropertyReader.readProperties(fileName)
    val builderObj = new UrlBuilder()
    val apiUrl = builderObj.buildUrl(props)
    val urlType = props.get("URL_TYPE").get
    if ("summary".equalsIgnoreCase(urlType))
      assert(apiUrl.equals(summaryUrl))
    else
      assert(apiUrl.equals(detailUrl))
  }

}