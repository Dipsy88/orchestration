/*
 * Copyright (c) 2014-2018 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.github.cloudiator.messaging;

import de.uniulm.omi.cloudiator.util.TwoWayConverter;
import io.github.cloudiator.domain.*;
import org.cloudiator.messages.NodeEntities;
import org.cloudiator.messages.NodeEntities.NodeProperties.Builder;

import java.util.stream.Collectors;

public class NodeToNodeMessageConverter implements TwoWayConverter<Node, NodeEntities.Node> {

  private final IpAddressMessageToIpAddress ipAddressConverter = new IpAddressMessageToIpAddress();
  private final NodeTypeToNodeTypeMessage nodeTypeConverter = new NodeTypeToNodeTypeMessage();
  private final NodePropertiesMessageToNodePropertiesConverter nodePropertiesConverter = new NodePropertiesMessageToNodePropertiesConverter();
  private final LoginCredentialMessageToLoginCredentialConverter loginCredentialConverter = new LoginCredentialMessageToLoginCredentialConverter();

  @Override
  public Node applyBack(NodeEntities.Node node) {

    final NodeBuilder nodeBuilder = NodeBuilder.newBuilder()
        .id(node.getId())
        .name(node.getName())
        .nodeProperties(nodePropertiesConverter.apply(node.getNodeProperties()))
        .nodeType(nodeTypeConverter.applyBack(node.getNodeType()))
        .ipAddresses(node.getIpAddressesList().stream()
            .map(ipAddressConverter::apply).collect(Collectors.toSet()));

    if (node.hasLoginCredential()) {
      nodeBuilder.loginCredential(loginCredentialConverter.apply(node.getLoginCredential()));
    }

    return nodeBuilder.build();
  }

  @Override
  public NodeEntities.Node apply(Node node) {

    final NodeEntities.Node.Builder builder = NodeEntities.Node.newBuilder().setId(node.id())
        .setName(node.name())
        .addAllIpAddresses(
            node.ipAddresses().stream().map(ipAddressConverter::applyBack)
                .collect(Collectors.toList()))
        .setNodeProperties(nodePropertiesConverter.applyBack(node.nodeProperties()))
        .setNodeType(nodeTypeConverter.apply(node.type()));

    if (node.loginCredential().isPresent()) {
      builder.setLoginCredential(loginCredentialConverter.applyBack(node.loginCredential().get()));
    }

    return builder.build();
  }

  private static class NodePropertiesMessageToNodePropertiesConverter implements
      TwoWayConverter<NodeEntities.NodeProperties, NodeProperties> {

    private final GeoLocationMessageToGeoLocationConverter geoLocationConverter = new GeoLocationMessageToGeoLocationConverter();
    private final OperatingSystemConverter operatingSystemConverter = new OperatingSystemConverter();

    @Override
    public NodeEntities.NodeProperties applyBack(NodeProperties nodeProperties) {
      final Builder builder = NodeEntities.NodeProperties.newBuilder()
          .setProviderId(nodeProperties.providerId())
          .setNumberOfCores(nodeProperties.numberOfCores()).setMemory(nodeProperties.memory());

      if (nodeProperties.geoLocation().isPresent()) {
        builder.setGeoLocation(geoLocationConverter.applyBack(nodeProperties.geoLocation().get()));
      }

      if (nodeProperties.operatingSystem().isPresent()) {
        builder.setOperationSystem(
            operatingSystemConverter.applyBack(nodeProperties.operatingSystem().get()));
      }

      if (nodeProperties.disk().isPresent()) {
        builder.setDisk(nodeProperties.disk().get());
      }

      return builder.build();

    }

    @Override
    public NodeProperties apply(NodeEntities.NodeProperties nodeProperties) {
      return NodePropertiesBuilder.newBuilder().providerId(nodeProperties.getProviderId())
          .numberOfCores(nodeProperties.getNumberOfCores())
          .disk(nodeProperties.getDisk())
          .geoLocation(geoLocationConverter.apply(nodeProperties.getGeoLocation()))
          .memory(nodeProperties.getMemory())
          .os(operatingSystemConverter.apply(nodeProperties.getOperationSystem())).build();
    }
  }

  private static class NodeTypeToNodeTypeMessage implements
      TwoWayConverter<NodeType, NodeEntities.NodeType> {

    @Override
    public NodeType applyBack(NodeEntities.NodeType nodeType) {
      switch (nodeType) {
        case VM:
          return NodeType.VM;
        case BYON:
          return NodeType.BYON;
        case CONTAINER:
          return NodeType.CONTAINER;
        case FAAS:
          return NodeType.FAAS;
        case UNKNOWN_TYPE:
          return NodeType.UNKOWN;
        case UNRECOGNIZED:
        default:
          throw new AssertionError(String.format("The nodeType %s is not known.", nodeType));
      }
    }

    @Override
    public NodeEntities.NodeType apply(NodeType nodeType) {
      switch (nodeType) {
        case VM:
          return NodeEntities.NodeType.VM;
        case BYON:
          return NodeEntities.NodeType.BYON;
        case UNKOWN:
          return NodeEntities.NodeType.UNKNOWN_TYPE;
        case CONTAINER:
          return NodeEntities.NodeType.CONTAINER;
        case FAAS:
          return NodeEntities.NodeType.FAAS;
        default:
          throw new AssertionError(String.format("The nodeType %s is not known.", nodeType));
      }
    }
  }
}
