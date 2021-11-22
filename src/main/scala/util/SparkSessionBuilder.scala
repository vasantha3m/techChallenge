package util

import org.apache.spark.sql.SparkSession

object SparkSessionBuilder {
  def getSparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("ReadFromAPI")
      .master("yarn")
      .enableHiveSupport()
      .getOrCreate()
}
