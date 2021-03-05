/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.daily_function;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.util.Log;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;

/**CameraX와 상호 작용하기위한 모델 보기 */
public final class CameraXViewModel extends AndroidViewModel {

  private static final String TAG = "CameraXViewModel";
  private MutableLiveData<ProcessCameraProvider> cameraProviderLiveData;

  /**
   * 주어진 애플리케이션 컨텍스트를 통해 카메라 서비스와 상호 작용하는 인스턴스 생성
   */
  public CameraXViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<ProcessCameraProvider> getProcessCameraProvider() {
    if (cameraProviderLiveData == null) {
      cameraProviderLiveData = new MutableLiveData<>();

      ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
          ProcessCameraProvider.getInstance(getApplication());
      cameraProviderFuture.addListener(
          () -> {
            try {
              cameraProviderLiveData.setValue(cameraProviderFuture.get());
            } catch (ExecutionException | InterruptedException e) {
              // Handle any errors (including cancellation) here.
              Log.e(TAG, "Unhandled exception", e);
            }
          },
          ContextCompat.getMainExecutor(getApplication()));
    }

    return cameraProviderLiveData;
  }
}
