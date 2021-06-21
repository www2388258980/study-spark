package com.yj.study.spark

import org.apache.spark.sql.SparkSession

object SparkSessionUtils {


  def getLocalSparkSession(): SparkSession = {
    SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.testing.memory", "471859200")
      .master("local[*]")
      .getOrCreate()
  }
}
