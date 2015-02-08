package com.logicalpractice.flume.bench

import com.codahale.metrics.MetricRegistry
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import io.dropwizard.util.Duration
import org.apache.flume.Event

/**
 *
 */
@Canonical
class MaxThroughput implements Runnable {
  EventSource source
  FlumeClient client
  Duration duration
  MetricRegistry metrics

  @CompileStatic
  void run() {
    Iterator<Event> events = source.events().iterator()

    RunTimer.runFor(duration) { int iterations ->
      FlumeClient localClient = client
      for( int i = 0; i < iterations; i ++ ) {
        localClient.send([events.next()])
      }
    }
  }
}
