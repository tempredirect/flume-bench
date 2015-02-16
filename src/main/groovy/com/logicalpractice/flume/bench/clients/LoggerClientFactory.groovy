package com.logicalpractice.flume.bench.clients

import com.logicalpractice.flume.bench.FlumeClient
import com.logicalpractice.flume.bench.FlumeClientFactory

/**
 *
 */
class LoggerClientFactory implements FlumeClientFactory {
  @Override
  String getName() {
    "logger"
  }

  @Override
  FlumeClient create() {
    new LoggerClient()
  }

  @Override
  void configure(Map<String, String> parameters) {

  }
}
