package com.yj.study.spark.sql.getting_started

import com.yj.study.spark.sql.SparkSessionUtils
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

object SecondRDD2Dataset {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionUtils.getLocalSparkSession()

    // Create an RDD
    val peopleRDD = spark.sparkContext
      .textFile(this.getClass.getResource("/people.txt").getPath)

    // The schema is encoded in a string
    val schemaString = "name age"

    // Generate the schema based on the string of schema
    val fields = schemaString.split(" ")
      .map(fieldname => StructField(fieldname, StringType, nullable = true))
    val schema = StructType(fields)

    // Convert records of the RDD (people) to Rows
    val rowRDD = peopleRDD
      .map(_.split(","))
      .map(attr => Row(attr(0),attr(1).trim))

    // Apply the schema to the RDD
    val peopleDF = spark.createDataFrame(rowRDD,schema)

    // Creates a temporary view using the DataFrame
    peopleDF.createOrReplaceTempView("people")

    // SQL can be run over a temporary view created using DataFrames
    val results = spark.sql("SELECT name FROM people")

    import spark.implicits._
    results.map(attributes => "Name: " + attributes(0)).show()
  }
}
