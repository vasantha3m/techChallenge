package readers

import java.io.{ IOException, UncheckedIOException }
import lombok.Generated
import org.apache.spark.sql.SparkSession
import scala.collection.mutable
import scala.annotation.tailrec
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.json4s.jackson.JsonMethods.parse

import util.{ SparkSessionBuilder, PropertyReader, UrlBuilder }
import writers.{ WriteToHBase, WriteToHive }
import model.{ Bikes, Result }
import traits.{ Writer }

object ReadFromBikeAPI {

  @Generated
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionBuilder.getSparkSession
    spark.sparkContext.setLogLevel("WARN")
    val props = PropertyReader.readProperties(args(0))

    val initialApiUrl = fetchUrlFromProps(props)
    execute(spark, props, initialApiUrl)
  }

  @tailrec
  def execute(spark: SparkSession, props: mutable.Map[String, String], apiUrl: String) {
    val httpGetObj = new HttpGet(apiUrl)
    val defaultHttplient = HttpClients.createDefault()
    val apiUrlResponse = defaultHttplient.execute(httpGetObj)
    val totalCount: String = {
      if (!props.contains("TOTAL_COUNT")) {
        props.put("TOTAL_COUNT", apiUrlResponse.getFirstHeader("Total").getValue)
      }
      props.get("TOTAL_COUNT").get
    }
    implicit val formats = org.json4s.DefaultFormats
    val apiResponseBody = parse(EntityUtils.toString(apiUrlResponse.getEntity(), "UTF-8")).extract[Result]

    import spark.implicits._
    val sourceDataset = spark.createDataset(apiResponseBody.bikes)
    val tableName = props.get("TABLE_NAME").get
    val writeInto = props.get("WRITE_INTO").get
    val writer = {
      if ("hbase".equalsIgnoreCase(writeInto))
        new WriteToHBase()
      else
        new WriteToHive()
    }
    writer.writeDataFrame(spark, sourceDataset, tableName)

    val totalPageCount = Math.ceil((totalCount.toDouble) / (props.get("PER_PAGE").get.toDouble))
    val currPage = props.get("PAGE_NO").get.toInt

    if (currPage < totalPageCount) {
      props("PAGE_NO") = String.valueOf(currPage + 1)
      val newUrl = fetchUrlFromProps(props)
      execute(spark, props, newUrl)
    }
  }

  @Generated
  def fetchUrlFromProps(props: mutable.Map[String, String]): String = {
    val urlTemplate = props.get("URL_TEMPLATE").get
    val urlType = props.get("URL_TYPE").get
    val apiUrl: String = {
      if ("summary".equalsIgnoreCase(urlType))
        urlTemplate
      else {
        val urlBuildObj = new UrlBuilder()
        urlBuildObj.buildUrl(props)
      }
    }
    apiUrl
  }
}

