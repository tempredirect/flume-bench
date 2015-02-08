package com.logicalpractice.flume.bench

import com.codahale.metrics.MetricRegistry
import com.google.common.net.HostAndPort
import org.apache.flume.Event

import java.util.concurrent.TimeUnit

/**
 *
 */
class MetricRecordingClient implements FlumeClient {

  final FlumeClient client
  final com.codahale.metrics.Timer timer

  MetricRecordingClient(FlumeClient client, MetricRegistry metricRegistry) {
    this.client = client
    this.timer = metricRegistry.timer(MetricRegistry.name("client"))
  }

  @Override
  void send(List<Event> events) {
    long start = System.nanoTime()
    client.send(events)
    timer.update(System.nanoTime() - start, TimeUnit.NANOSECONDS)
  }

  @Override
  void initialise(HostAndPort hostAndPort, Map<String, String> parameter) {
    client.initialise(hostAndPort, parameter)
  }

  @Override
  String getName() {
    return "notVisible"
  }
}
