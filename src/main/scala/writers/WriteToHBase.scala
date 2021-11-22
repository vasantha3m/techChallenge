package writers

import org.apache.spark.sql.{ SparkSession, Dataset }
import org.apache.hadoop.hbase.client.{ ConnectionFactory, ColumnFamilyDescriptorBuilder, TableDescriptorBuilder }
import org.apache.hadoop.hbase.{ HBaseConfiguration, TableName }
import org.apache.spark.sql.datasources.hbase.{ HBaseTableCatalog }
import lombok.Generated

import schemas.HBaseBikeSchema
import traits.Writer

@Generated
class WriteToHBase extends Writer {

  override def writeDataFrame(
    spark:        SparkSession,
    dfToWrite:    Dataset[model.Bikes],
    tableNameStr: String) {
    val tableName = TableName.valueOf(tableNameStr)
    val colFamilyBike = "bike_info".getBytes
    val colFamilyStolenBike = "stolen_info".getBytes
    val colFamilyOther = "other_info".getBytes

    val hBaseConf = HBaseConfiguration.create()
    val connection = ConnectionFactory.createConnection(hBaseConf);
    val admin = connection.getAdmin();

    val tableExistsCheck: Boolean = admin.tableExists(tableName)

    if (tableExistsCheck == false) {
      val colFamilyBikeBuilder = ColumnFamilyDescriptorBuilder.newBuilder(colFamilyBike).setMaxVersions(3).build()
      val colFamilyStolenBikeBuilder = ColumnFamilyDescriptorBuilder.newBuilder(colFamilyStolenBike).setMaxVersions(3).build()
      val colFamilyOtherBuilder = ColumnFamilyDescriptorBuilder.newBuilder(colFamilyOther).setMaxVersions(3).build()
      val tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName)
        .setColumnFamily(colFamilyBikeBuilder)
        .setColumnFamily(colFamilyStolenBikeBuilder)
        .setColumnFamily(colFamilyOtherBuilder)
        .build()
      admin.createTable(tableDescriptorBuilder)
    }

    dfToWrite.write.options(
      Map(HBaseTableCatalog.tableCatalog -> HBaseBikeSchema.tableSchema, HBaseTableCatalog.newTable -> "3"))
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }
}