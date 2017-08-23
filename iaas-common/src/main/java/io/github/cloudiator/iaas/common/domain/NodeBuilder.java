package io.github.cloudiator.iaas.common.domain;

import de.uniulm.omi.cloudiator.sword.domain.IpAddress;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import java.util.Set;

public class NodeBuilder {

  private NodeProperties nodeProperties;
  private LoginCredential loginCredential;
  private NodeType nodeType;
  private Set<IpAddress> ipAddresses;
  private String id;

  public NodeBuilder nodeProperties(
      NodeProperties nodeProperties) {
    this.nodeProperties = nodeProperties;
    return this;
  }

  public NodeBuilder loginCredential(
      LoginCredential loginCredential) {
    this.loginCredential = loginCredential;
    return this;
  }

  public NodeBuilder nodeType(NodeType nodeType) {
    this.nodeType = nodeType;
    return this;
  }

  public NodeBuilder ipAddresses(
      Set<IpAddress> ipAddresses) {
    this.ipAddresses = ipAddresses;
    return this;
  }

  public NodeBuilder id(String id) {
    this.id = id;
    return this;
  }

  public Node build() {
    return new NodeImpl(nodeProperties, loginCredential, nodeType, ipAddresses, id);
  }

  private NodeBuilder() {
  }

  public static NodeBuilder newBuilder() {
    return new NodeBuilder();
  }


}
