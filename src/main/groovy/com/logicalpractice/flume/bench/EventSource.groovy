package com.logicalpractice.flume.bench

import com.google.common.base.Charsets
import com.google.common.collect.Iterables
import org.apache.flume.Event
import org.apache.flume.event.EventBuilder
import org.apache.flume.event.SimpleEvent

/**
 *
 */
class EventSource {

  List<Event> events

  EventSource() {
    events = (1..10).collect {
      EventBuilder.withBody("127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326",
          Charsets.UTF_8, [
          "hostname": "www1.example.com",
          "timestamp": System.currentTimeMillis().toString()
      ])
    }
  }

  Iterable<Event> events() {
    Iterables.cycle(events)
  }

}
