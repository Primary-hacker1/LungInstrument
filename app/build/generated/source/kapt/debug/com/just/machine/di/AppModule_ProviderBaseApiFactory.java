// Generated by Dagger (https://dagger.dev).
package com.just.machine.di;

import com.just.machine.api.BaseApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class AppModule_ProviderBaseApiFactory implements Factory<BaseApiService> {
  @Override
  public BaseApiService get() {
    return providerBaseApi();
  }

  public static AppModule_ProviderBaseApiFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BaseApiService providerBaseApi() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providerBaseApi());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProviderBaseApiFactory INSTANCE = new AppModule_ProviderBaseApiFactory();
  }
}