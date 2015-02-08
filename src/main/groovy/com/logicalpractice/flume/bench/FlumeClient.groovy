package com.logicalpractice.flume.bench

import com.google.common.net.HostAndPort
import org.apache.flume.Event

/**
 *
 */
interface FlumeClient {

  void send(List<Event> events)

  void initialise(HostAndPort hostAndPort, Map<String,String> parameter)

  String getName()
}
