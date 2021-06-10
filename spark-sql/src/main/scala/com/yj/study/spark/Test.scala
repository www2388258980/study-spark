package com.yj.study.spark

import org.apache.spark.sql.SparkSession

object Test {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
//      .config("spark.testing.memory", "471859200")
      .master("local[*]")
      .getOrCreate()

    val df = spark.read.json(this.getClass.getResource("/people.json").getPath)
    df.show()
  }
}
