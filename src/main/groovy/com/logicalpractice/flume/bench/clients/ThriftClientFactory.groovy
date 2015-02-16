package com.logicalpractice.flume.bench.clients

import com.logicalpractice.flume.bench.FlumeClient
import com.logicalpractice.flume.bench.FlumeClientFactory

/**
 *
 */
class ThriftClientFactory implements FlumeClientFactory {

  @Override
  String getName() {
    "thrift"
  }

  @Override
  FlumeClient create() {
    new ThriftClient()
  }

  @Override
  void configure(Map<String, String> parameters) {

  }
}
