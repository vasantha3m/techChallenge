package traits

import org.apache.spark.sql.{ SparkSession, Dataset }

abstract class Writer {
  def writeDataFrame(
    spark:        SparkSession,
    dfToWrite:    Dataset[model.Bikes],
    tableNameStr: String)

}