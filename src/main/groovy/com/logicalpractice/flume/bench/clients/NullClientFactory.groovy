package com.logicalpractice.flume.bench.clients

import com.logicalpractice.flume.bench.FlumeClient
import com.logicalpractice.flume.bench.FlumeClientFactory

/**
 * factory for the null client.
 */
class NullClientFactory implements FlumeClientFactory {

  @Override
  String getName() {
    "null"
  }

  @Override
  FlumeClient create() {
    new NullClient()
  }

  @Override
  void configure(Map<String, String> parameters) {

  }
}
