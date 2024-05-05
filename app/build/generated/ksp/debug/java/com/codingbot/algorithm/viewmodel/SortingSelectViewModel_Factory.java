package com.codingbot.algorithm.viewmodel;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SortingSelectViewModel_Factory implements Factory<SortingSelectViewModel> {
  @Override
  public SortingSelectViewModel get() {
    return newInstance();
  }

  public static SortingSelectViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SortingSelectViewModel newInstance() {
    return new SortingSelectViewModel();
  }

  private static final class InstanceHolder {
    private static final SortingSelectViewModel_Factory INSTANCE = new SortingSelectViewModel_Factory();
  }
}
