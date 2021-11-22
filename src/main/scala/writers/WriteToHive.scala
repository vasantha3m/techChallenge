package writers

import org.apache.spark.sql.{ SparkSession, Dataset, SaveMode }
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

import traits.Writer

class WriteToHive extends Writer {
  override def writeDataFrame(
    spark:     SparkSession,
    dfToWrite: Dataset[model.Bikes], tableNameStr: String) {
    dfToWrite.write.mode(SaveMode.Append).saveAsTable("staging_" + tableNameStr)

    val windowSpec = Window.partitionBy("id").orderBy(col("date_stolen").desc)
    val dfWithVersion = dfToWrite.withColumn("VERSION_NO", dense_rank().over(windowSpec))
    dfWithVersion.write.mode(SaveMode.Overwrite).saveAsTable(tableNameStr)
  }
}