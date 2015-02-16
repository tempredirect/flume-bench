package com.logicalpractice.flume.bench

/**
 *
 */
interface FlumeClientFactory {

  String getName()

  FlumeClient create()

  void configure(Map<String,String> parameters)
}
