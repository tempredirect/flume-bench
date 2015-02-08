package com.logicalpractice.flume.bench.clients

import com.google.common.net.HostAndPort
import com.logicalpractice.flume.bench.FlumeClient
import org.apache.flume.Event

/**
 *
 */
class NullClient implements FlumeClient {

  @Override
  void send(List<Event> events) {
  }

  @Override
  void initialise(HostAndPort hostAndPort, Map<String, String> parameter) {

  }

  @Override
  String getName() {
    "null"
  }
}
