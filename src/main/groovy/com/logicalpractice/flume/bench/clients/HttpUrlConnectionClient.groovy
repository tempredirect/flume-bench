package com.logicalpractice.flume.bench.clients

import com.google.common.io.ByteStreams
import com.google.common.net.HostAndPort
import com.logicalpractice.flume.bench.FlumeClient
import groovy.json.StreamingJsonBuilder
import org.apache.flume.Event

/**
 *
 */
class HttpUrlConnectionClient implements FlumeClient {

  HostAndPort hostAndPort

  @Override
  void send(List<Event> events) {
    URL url = new URL("http://$hostAndPort.hostText:$hostAndPort.port/")
    HttpURLConnection connection = (HttpURLConnection)url.openConnection()

    connection.setRequestMethod('POST')
    connection.setDoOutput(true)
    connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
    OutputStreamWriter writer = new OutputStreamWriter(connection.outputStream, 'UTF-8')
    StreamingJsonBuilder builder = new StreamingJsonBuilder(writer)
    builder(events.collect { e -> [headers: e.headers, body: new String(e.body, 'UTF-8')]})
    writer.flush()
    writer.close()

    int code = connection.getResponseCode()


    if (code != 200) {
      ByteStreams.toByteArray(connection.errorStream)
    } else {
      ByteStreams.toByteArray(connection.inputStream)
    }
  }

  @Override
  void initialise(HostAndPort hostAndPort) {
    this.hostAndPort = hostAndPort
  }
}
