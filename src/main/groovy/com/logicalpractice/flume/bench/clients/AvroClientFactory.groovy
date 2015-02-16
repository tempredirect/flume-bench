package com.logicalpractice.flume.bench.clients

import com.logicalpractice.flume.bench.FlumeClient
import com.logicalpractice.flume.bench.FlumeClientFactory

/**
 *
 */
class AvroClientFactory implements FlumeClientFactory {

  @Override
  String getName() {
    return "avro"
  }

  @Override
  FlumeClient create() {
    return new AvroClient()
  }

  @Override
  void configure(Map<String, String> parameters) {

  }
}
