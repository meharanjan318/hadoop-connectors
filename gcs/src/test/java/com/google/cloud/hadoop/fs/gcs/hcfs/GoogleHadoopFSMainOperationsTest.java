/*
 * Copyright 2014 Google Inc. All Rights Reserved.
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

package com.google.cloud.hadoop.fs.gcs.hcfs;

import static com.google.common.truth.Truth.assertThat;
import static org.apache.hadoop.fs.FileSystemTestHelper.exists;
import static org.junit.Assert.assertThrows;

import com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystemTestHelper;
import java.io.IOException;
import org.apache.hadoop.fs.FSMainOperationsBaseTest;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileSystemTestHelper;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Runs the Hadoop tests in FSMainOperationsBaseTest over the GoogleHadoopFileSystem. Tests that the
 * GoogleHadoopFileSystem obeys the file system contract specified for Hadoop.
 */
@RunWith(JUnit4.class)
public class GoogleHadoopFSMainOperationsTest extends FSMainOperationsBaseTest {

  private final FileSystemTestHelper helper = new FileSystemTestHelper();

  /**
   * In Hadoop2 this method overrides the abstract method of the same name in
   * FSMainOperationsBaseTest
   */
  public FileSystem createFileSystem() throws Exception {
    return GoogleHadoopFileSystemTestHelper.createInMemoryGoogleHadoopFileSystem();
  }

  /**
   * Copied from FSMainOperationsBaseTest.java with the only changes being throwing an IOException
   * instead of returning false when trying to create directories on top of existing files. This
   * behavior is in-line with HDFS, but differs from LocalFileSystem.
   */
  @Test
  @Override
  public void testMkdirsFailsForSubdirectoryOfExistingFile() throws Exception {
    Path testDir = helper.getTestRootPath(fSys, "test/hadoop");
    assertThat(exists(fSys, testDir)).isFalse();
    fSys.mkdirs(testDir);
    assertThat(exists(fSys, testDir)).isTrue();

    createFile(helper.getTestRootPath(fSys, "test/hadoop/file"));

    Path testSubDir = helper.getTestRootPath(fSys, "test/hadoop/file/subdir");
    assertThrows(IOException.class, () -> fSys.mkdirs(testSubDir));
    assertThat(exists(fSys, testSubDir)).isFalse();

    Path testDeepSubDir = helper.getTestRootPath(fSys, "test/hadoop/file/deep/sub/dir");
    assertThat(exists(fSys, testSubDir)).isFalse();
    assertThrows(IOException.class, () -> fSys.mkdirs(testDeepSubDir));
    assertThat(exists(fSys, testDeepSubDir)).isFalse();
  }

  // Ignore: FS-level permissions are not supported in GCS
  @Override
  public void testListStatusThrowsExceptionForUnreadableDir() {}

  // Ignore: FS-level permissions are not supported in GCS
  @Test
  public void testGlobStatusThrowsExceptionForUnreadableDir() {}

  // Ignore: unit tests can not access Local FS
  @Override
  public void testCopyToLocalWithUseRawLocalFileSystemOption() {}
}
