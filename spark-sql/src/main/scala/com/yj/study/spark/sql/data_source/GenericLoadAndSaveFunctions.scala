package com.yj.study.spark.sql.data_source

import com.yj.study.spark.sql.{LoggerTrait, SparkSessionUtils}
import org.apache.spark.sql.SaveMode

// Generic Load/Save Functions
object GenericLoadAndSaveFunctions extends LoggerTrait {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionUtils.getLocalSparkSession()

    val usersDF = spark.read.load(this.getClass.getResource("/parquet/users.parquet").getPath)
    usersDF.select("favorite_color").write
      .mode(SaveMode.Overwrite)
      .save(this.getClass.getResource("/parquet/favorite_color.parquet").getPath)

    val df = spark.read.json(this.getClass.getResource("/people.json").getPath)

    df.select("name", "age").write
      .mode(SaveMode.Overwrite)
      .format("parquet").save("namesAndAges.parquet")

    df.select("name", "age").write
      .mode(SaveMode.Append)
      .format("json")
      .save("favorite_color")


    val peopleCSV = spark.read.format("csv")
      .load(this.getClass.getResource("/csv/people.csv").getPath)
    peopleCSV.show()

    usersDF.write.format("orc")
      .option("orc.bloom.filter.columns", "favorite_color")
      .option("orc.dictionary.key.threshold", "1.0")
      .option("orc.column.encoding.direct", "name")
      .mode(SaveMode.Overwrite)
      .save("users_with_options.orc")


    val sqlDF = spark.sql("select * from parquet.`" +
      this.getClass.getResource("/parquet/users.parquet").getPath + "`")
    sqlDF.show()


    peopleCSV.write.bucketBy(42, "_c0").sortBy("_c1")
      .option("path", this.getClass.getResource("/warehouse").getPath)
      .saveAsTable("people_bucket")
    spark.sql("select * from people_bucket where _c0='张三1'").show()

    peopleCSV.write.mode(SaveMode.Overwrite)
      .partitionBy("_c2")
      .bucketBy(42, "_c0")
      .saveAsTable("users_partitioned_bucketed")
    spark.sql("select * from users_partitioned_bucketed where _c2='男'").show()


    peopleCSV.write.partitionBy("_c2").format("json")
      .saveAsTable("PartitionBySex")
    spark.sql("select * from PartitionBySex where _c2='男'").show()

  }
}
