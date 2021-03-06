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

package io.github.cloudiator.persistance;

import static com.google.common.base.Preconditions.checkNotNull;

import io.github.cloudiator.domain.DiscoveryItemState;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
class HardwareModel extends ResourceModel {

  @ManyToOne(optional = false)
  private HardwareOfferModel hardwareOfferModel;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DiscoveryItemState state;

  /**
   * Empty constructor for hibernate.
   */
  protected HardwareModel() {
  }

  public HardwareModel(String cloudUniqueId, String providerId, String name,
      CloudModel cloudModel, @Nullable LocationModel locationModel,
      HardwareOfferModel hardwareOfferModel, DiscoveryItemState state) {
    super(cloudUniqueId, providerId, name, cloudModel, locationModel);
    checkNotNull(hardwareOfferModel, "hardwareOfferModel is null");
    this.hardwareOfferModel = hardwareOfferModel;
    checkNotNull(state, "state is null");
    this.state = state;
  }

  public HardwareOfferModel hardwareOffer() {
    return hardwareOfferModel;
  }

  public HardwareModel setHardwareOfferModel(
      HardwareOfferModel hardwareOfferModel) {
    this.hardwareOfferModel = hardwareOfferModel;
    return this;
  }

  public DiscoveryItemState getState() {
    return state;
  }

  public HardwareModel setState(DiscoveryItemState state) {
    this.state = state;
    return this;
  }

  public TenantModel getTenant() {
    return getCloudModel().getTenantModel();
  }
}
