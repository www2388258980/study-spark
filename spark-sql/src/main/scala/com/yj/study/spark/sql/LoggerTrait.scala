package com.yj.study.spark.sql

import org.apache.log4j.{Level, Logger}

trait LoggerTrait {

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
  Logger.getLogger("org.spark_project").setLevel(Level.WARN)
}
