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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
class CloudCredentialModel extends Model {

  @Lob
  @Column(nullable = false, updatable = false)
  private String user;
  @Lob
  @Column(nullable = false)
  private String password;

  /**
   * Empty constructor for hibernate.
   */
  protected CloudCredentialModel() {
  }

  public CloudCredentialModel(String user, String password) {

    checkNotNull(user);
    checkNotNull(password);
    checkArgument(!user.isEmpty());
    checkArgument(!password.isEmpty());

    this.user = user;
    this.password = password;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public CloudCredentialModel setPassword(String password) {
    this.password = password;
    return this;
  }
}
