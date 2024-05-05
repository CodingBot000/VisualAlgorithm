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
public final class SortingHeapSortingViewModel_Factory implements Factory<SortingHeapSortingViewModel> {
  @Override
  public SortingHeapSortingViewModel get() {
    return newInstance();
  }

  public static SortingHeapSortingViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SortingHeapSortingViewModel newInstance() {
    return new SortingHeapSortingViewModel();
  }

  private static final class InstanceHolder {
    private static final SortingHeapSortingViewModel_Factory INSTANCE = new SortingHeapSortingViewModel_Factory();
  }
}
