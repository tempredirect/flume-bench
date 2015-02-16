package com.logicalpractice.flume.bench.clients

import com.logicalpractice.flume.bench.FlumeClient
import com.logicalpractice.flume.bench.FlumeClientFactory

/**
 *
 */
class HttpUrlConnectionClientFactory implements FlumeClientFactory {
  @Override
  String getName() {
    return "http"
  }

  @Override
  FlumeClient create() {
    new HttpUrlConnectionClient()
  }

  @Override
  void configure(Map<String, String> parameters) {

  }
}
