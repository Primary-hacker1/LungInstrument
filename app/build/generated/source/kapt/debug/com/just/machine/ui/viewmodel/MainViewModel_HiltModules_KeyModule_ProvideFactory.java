// Generated by Dagger (https://dagger.dev).
package com.just.machine.ui.viewmodel;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MainViewModel_HiltModules_KeyModule_ProvideFactory implements Factory<String> {
  @Override
  public String get() {
    return provide();
  }

  public static MainViewModel_HiltModules_KeyModule_ProvideFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static String provide() {
    return Preconditions.checkNotNullFromProvides(MainViewModel_HiltModules.KeyModule.provide());
  }

  private static final class InstanceHolder {
    private static final MainViewModel_HiltModules_KeyModule_ProvideFactory INSTANCE = new MainViewModel_HiltModules_KeyModule_ProvideFactory();
  }
}
