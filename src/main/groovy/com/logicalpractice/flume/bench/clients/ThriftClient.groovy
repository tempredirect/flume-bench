package com.logicalpractice.flume.bench.clients

import com.google.common.net.HostAndPort
import org.apache.flume.api.RpcClientFactory

/**
 *
 */
class ThriftClient extends AbstractRpcClient {

  @Override
  void initialise(HostAndPort hostAndPort) {
    rpcClient = RpcClientFactory.getThriftInstance(hostAndPort.hostText, hostAndPort.port)
  }
}
