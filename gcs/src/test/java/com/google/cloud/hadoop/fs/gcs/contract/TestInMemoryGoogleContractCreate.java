/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.hadoop.fs.gcs.contract;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.contract.AbstractContractCreateTest;
import org.apache.hadoop.fs.contract.AbstractFSContract;
import org.apache.hadoop.fs.contract.ContractTestUtils;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestInMemoryGoogleContractCreate extends AbstractContractCreateTest {

  @Override
  protected AbstractFSContract createContract(Configuration conf) {
    return new InMemoryGoogleContract(conf);
  }

  @Override
  public void testOverwriteEmptyDirectory() throws Throwable {
    ContractTestUtils.skip("blobstores can't distinguish empty directories from files");
  }
}
