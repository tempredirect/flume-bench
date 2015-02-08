package com.logicalpractice.flume.bench.clients

import com.google.common.net.HostAndPort
import com.logicalpractice.flume.bench.FlumeClient
import groovy.util.logging.Slf4j
import org.apache.flume.Event

/**
 *
 */
@Slf4j
class LoggerClient implements FlumeClient {

  @Override
  void send(List<Event> events) {
    log.info("$events")
  }

  @Override
  void initialise(HostAndPort hostAndPort, Map<String, String> parameter) {

  }

  @Override
  String getName() {
    "logger"
  }
}
