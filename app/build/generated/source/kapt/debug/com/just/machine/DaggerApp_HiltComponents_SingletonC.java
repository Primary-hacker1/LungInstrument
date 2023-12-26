// Generated by Dagger (https://dagger.dev).
package com.just.machine;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.just.machine.api.BaseApiService;
import com.just.machine.api.UserRepository;
import com.just.machine.dao.AppDatabase;
import com.just.machine.dao.PlantDao;
import com.just.machine.dao.PlantRepository;
import com.just.machine.di.AppModule;
import com.just.machine.di.AppModule_ProviderBaseApiFactory;
import com.just.machine.di.DatabaseModule;
import com.just.machine.di.DatabaseModule_ProvideAppDatabaseFactory;
import com.just.machine.di.DatabaseModule_ProvidePlantDaoFactory;
import com.just.machine.ui.activity.LoginActivity;
import com.just.machine.ui.activity.MainActivity;
import com.just.machine.ui.activity.WelComeActivity;
import com.just.machine.ui.fragment.LoginFragment;
import com.just.machine.ui.fragment.MainFragment;
import com.just.machine.ui.fragment.MeFragment;
import com.just.machine.ui.fragment.NewFragment;
import com.just.machine.ui.fragment.SettingFragment;
import com.just.machine.ui.viewmodel.MainViewModel;
import com.just.machine.ui.viewmodel.MainViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_Lifecycle_Factory;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MemoizedSentinel;
import dagger.internal.Preconditions;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerApp_HiltComponents_SingletonC extends App_HiltComponents.SingletonC {
  private final ApplicationContextModule applicationContextModule;

  private final DaggerApp_HiltComponents_SingletonC singletonC = this;

  private volatile Object baseApiService = new MemoizedSentinel();

  private volatile Object appDatabase = new MemoizedSentinel();

  private volatile Object plantDao = new MemoizedSentinel();

  private DaggerApp_HiltComponents_SingletonC(
      ApplicationContextModule applicationContextModuleParam) {
    this.applicationContextModule = applicationContextModuleParam;
  }

  public static Builder builder() {
    return new Builder();
  }

  private BaseApiService baseApiService() {
    Object local = baseApiService;
    if (local instanceof MemoizedSentinel) {
      synchronized (local) {
        local = baseApiService;
        if (local instanceof MemoizedSentinel) {
          local = AppModule_ProviderBaseApiFactory.providerBaseApi();
          baseApiService = DoubleCheck.reentrantCheck(baseApiService, local);
        }
      }
    }
    return (BaseApiService) local;
  }

  private AppDatabase appDatabase() {
    Object local = appDatabase;
    if (local instanceof MemoizedSentinel) {
      synchronized (local) {
        local = appDatabase;
        if (local instanceof MemoizedSentinel) {
          local = DatabaseModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
          appDatabase = DoubleCheck.reentrantCheck(appDatabase, local);
        }
      }
    }
    return (AppDatabase) local;
  }

  private PlantDao plantDao() {
    Object local = plantDao;
    if (local instanceof MemoizedSentinel) {
      synchronized (local) {
        local = plantDao;
        if (local instanceof MemoizedSentinel) {
          local = DatabaseModule_ProvidePlantDaoFactory.providePlantDao(appDatabase());
          plantDao = DoubleCheck.reentrantCheck(plantDao, local);
        }
      }
    }
    return (PlantDao) local;
  }

  @Override
  public void injectApp(App app) {
  }

  @Override
  public ActivityRetainedComponentBuilder retainedComponentBuilder() {
    return new ActivityRetainedCBuilder(singletonC);
  }

  @Override
  public ServiceComponentBuilder serviceComponentBuilder() {
    return new ServiceCBuilder(singletonC);
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder appModule(AppModule appModule) {
      Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder databaseModule(DatabaseModule databaseModule) {
      Preconditions.checkNotNull(databaseModule);
      return this;
    }

    public App_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new DaggerApp_HiltComponents_SingletonC(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements App_HiltComponents.ActivityRetainedC.Builder {
    private final DaggerApp_HiltComponents_SingletonC singletonC;

    private ActivityRetainedCBuilder(DaggerApp_HiltComponents_SingletonC singletonC) {
      this.singletonC = singletonC;
    }

    @Override
    public App_HiltComponents.ActivityRetainedC build() {
      return new ActivityRetainedCImpl(singletonC);
    }
  }

  private static final class ActivityRetainedCImpl extends App_HiltComponents.ActivityRetainedC {
    private final DaggerApp_HiltComponents_SingletonC singletonC;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private volatile Object lifecycle = new MemoizedSentinel();

    private ActivityRetainedCImpl(DaggerApp_HiltComponents_SingletonC singletonC) {
      this.singletonC = singletonC;

    }

    private Object lifecycle() {
      Object local = lifecycle;
      if (local instanceof MemoizedSentinel) {
        synchronized (local) {
          local = lifecycle;
          if (local instanceof MemoizedSentinel) {
            local = ActivityRetainedComponentManager_Lifecycle_Factory.newInstance();
            lifecycle = DoubleCheck.reentrantCheck(lifecycle, local);
          }
        }
      }
      return (Object) local;
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonC, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return (ActivityRetainedLifecycle) lifecycle();
    }

    private static final class ActivityCBuilder implements App_HiltComponents.ActivityC.Builder {
      private final DaggerApp_HiltComponents_SingletonC singletonC;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private Activity activity;

      private ActivityCBuilder(DaggerApp_HiltComponents_SingletonC singletonC,
          ActivityRetainedCImpl activityRetainedCImpl) {
        this.singletonC = singletonC;
        this.activityRetainedCImpl = activityRetainedCImpl;
      }

      @Override
      public ActivityCBuilder activity(Activity activity) {
        this.activity = Preconditions.checkNotNull(activity);
        return this;
      }

      @Override
      public App_HiltComponents.ActivityC build() {
        Preconditions.checkBuilderRequirement(activity, Activity.class);
        return new ActivityCImpl(singletonC, activityRetainedCImpl, activity);
      }
    }

    private static final class ActivityCImpl extends App_HiltComponents.ActivityC {
      private final DaggerApp_HiltComponents_SingletonC singletonC;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ActivityCImpl activityCImpl = this;

      private ActivityCImpl(DaggerApp_HiltComponents_SingletonC singletonC,
          ActivityRetainedCImpl activityRetainedCImpl, Activity activity) {
        this.singletonC = singletonC;
        this.activityRetainedCImpl = activityRetainedCImpl;

      }

      @Override
      public void injectLoginActivity(LoginActivity loginActivity) {
      }

      @Override
      public void injectMainActivity(MainActivity mainActivity) {
      }

      @Override
      public void injectWelComeActivity(WelComeActivity welComeActivity) {
      }

      @Override
      public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
        return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(ApplicationContextModule_ProvideApplicationFactory.provideApplication(singletonC.applicationContextModule), getViewModelKeys(), new ViewModelCBuilder(singletonC, activityRetainedCImpl));
      }

      @Override
      public Set<String> getViewModelKeys() {
        return Collections.<String>singleton(MainViewModel_HiltModules_KeyModule_ProvideFactory.provide());
      }

      @Override
      public ViewModelComponentBuilder getViewModelComponentBuilder() {
        return new ViewModelCBuilder(singletonC, activityRetainedCImpl);
      }

      @Override
      public FragmentComponentBuilder fragmentComponentBuilder() {
        return new FragmentCBuilder(singletonC, activityRetainedCImpl, activityCImpl);
      }

      @Override
      public ViewComponentBuilder viewComponentBuilder() {
        return new ViewCBuilder(singletonC, activityRetainedCImpl, activityCImpl);
      }

      private static final class FragmentCBuilder implements App_HiltComponents.FragmentC.Builder {
        private final DaggerApp_HiltComponents_SingletonC singletonC;

        private final ActivityRetainedCImpl activityRetainedCImpl;

        private final ActivityCImpl activityCImpl;

        private Fragment fragment;

        private FragmentCBuilder(DaggerApp_HiltComponents_SingletonC singletonC,
            ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
          this.singletonC = singletonC;
          this.activityRetainedCImpl = activityRetainedCImpl;
          this.activityCImpl = activityCImpl;
        }

        @Override
        public FragmentCBuilder fragment(Fragment fragment) {
          this.fragment = Preconditions.checkNotNull(fragment);
          return this;
        }

        @Override
        public App_HiltComponents.FragmentC build() {
          Preconditions.checkBuilderRequirement(fragment, Fragment.class);
          return new FragmentCI(singletonC, activityRetainedCImpl, activityCImpl, fragment);
        }
      }

      private static final class FragmentCI extends App_HiltComponents.FragmentC {
        private final DaggerApp_HiltComponents_SingletonC singletonC;

        private final ActivityRetainedCImpl activityRetainedCImpl;

        private final ActivityCImpl activityCImpl;

        private final FragmentCI fragmentCI = this;

        private FragmentCI(DaggerApp_HiltComponents_SingletonC singletonC,
            ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
            Fragment fragment) {
          this.singletonC = singletonC;
          this.activityRetainedCImpl = activityRetainedCImpl;
          this.activityCImpl = activityCImpl;

        }

        @Override
        public void injectLoginFragment(LoginFragment loginFragment) {
        }

        @Override
        public void injectMainFragment(MainFragment mainFragment) {
        }

        @Override
        public void injectMeFragment(MeFragment meFragment) {
        }

        @Override
        public void injectNewFragment(NewFragment newFragment) {
        }

        @Override
        public void injectSettingFragment(SettingFragment settingFragment) {
        }

        @Override
        public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
          return activityCImpl.getHiltInternalFactoryFactory();
        }

        @Override
        public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
          return new ViewWithFragmentCBuilder(singletonC, activityRetainedCImpl, activityCImpl, fragmentCI);
        }

        private static final class ViewWithFragmentCBuilder implements App_HiltComponents.ViewWithFragmentC.Builder {
          private final DaggerApp_HiltComponents_SingletonC singletonC;

          private final ActivityRetainedCImpl activityRetainedCImpl;

          private final ActivityCImpl activityCImpl;

          private final FragmentCI fragmentCI;

          private View view;

          private ViewWithFragmentCBuilder(DaggerApp_HiltComponents_SingletonC singletonC,
              ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
              FragmentCI fragmentCI) {
            this.singletonC = singletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
            this.activityCImpl = activityCImpl;
            this.fragmentCI = fragmentCI;
          }

          @Override
          public ViewWithFragmentCBuilder view(View view) {
            this.view = Preconditions.checkNotNull(view);
            return this;
          }

          @Override
          public App_HiltComponents.ViewWithFragmentC build() {
            Preconditions.checkBuilderRequirement(view, View.class);
            return new ViewWithFragmentCI(singletonC, activityRetainedCImpl, activityCImpl, fragmentCI, view);
          }
        }

        private static final class ViewWithFragmentCI extends App_HiltComponents.ViewWithFragmentC {
          private final DaggerApp_HiltComponents_SingletonC singletonC;

          private final ActivityRetainedCImpl activityRetainedCImpl;

          private final ActivityCImpl activityCImpl;

          private final FragmentCI fragmentCI;

          private final ViewWithFragmentCI viewWithFragmentCI = this;

          private ViewWithFragmentCI(DaggerApp_HiltComponents_SingletonC singletonC,
              ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
              FragmentCI fragmentCI, View view) {
            this.singletonC = singletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
            this.activityCImpl = activityCImpl;
            this.fragmentCI = fragmentCI;

          }
        }
      }

      private static final class ViewCBuilder implements App_HiltComponents.ViewC.Builder {
        private final DaggerApp_HiltComponents_SingletonC singletonC;

        private final ActivityRetainedCImpl activityRetainedCImpl;

        private final ActivityCImpl activityCImpl;

        private View view;

        private ViewCBuilder(DaggerApp_HiltComponents_SingletonC singletonC,
            ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
          this.singletonC = singletonC;
          this.activityRetainedCImpl = activityRetainedCImpl;
          this.activityCImpl = activityCImpl;
        }

        @Override
        public ViewCBuilder view(View view) {
          this.view = Preconditions.checkNotNull(view);
          return this;
        }

        @Override
        public App_HiltComponents.ViewC build() {
          Preconditions.checkBuilderRequirement(view, View.class);
          return new ViewCI(singletonC, activityRetainedCImpl, activityCImpl, view);
        }
      }

      private static final class ViewCI extends App_HiltComponents.ViewC {
        private final DaggerApp_HiltComponents_SingletonC singletonC;

        private final ActivityRetainedCImpl activityRetainedCImpl;

        private final ActivityCImpl activityCImpl;

        private final ViewCI viewCI = this;

        private ViewCI(DaggerApp_HiltComponents_SingletonC singletonC,
            ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl, View view) {
          this.singletonC = singletonC;
          this.activityRetainedCImpl = activityRetainedCImpl;
          this.activityCImpl = activityCImpl;

        }
      }
    }

    private static final class ViewModelCBuilder implements App_HiltComponents.ViewModelC.Builder {
      private final DaggerApp_HiltComponents_SingletonC singletonC;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private SavedStateHandle savedStateHandle;

      private ViewModelCBuilder(DaggerApp_HiltComponents_SingletonC singletonC,
          ActivityRetainedCImpl activityRetainedCImpl) {
        this.singletonC = singletonC;
        this.activityRetainedCImpl = activityRetainedCImpl;
      }

      @Override
      public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
        this.savedStateHandle = Preconditions.checkNotNull(handle);
        return this;
      }

      @Override
      public App_HiltComponents.ViewModelC build() {
        Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
        return new ViewModelCImpl(singletonC, activityRetainedCImpl, savedStateHandle);
      }
    }

    private static final class ViewModelCImpl extends App_HiltComponents.ViewModelC {
      private final DaggerApp_HiltComponents_SingletonC singletonC;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl = this;

      private volatile Provider<MainViewModel> mainViewModelProvider;

      private ViewModelCImpl(DaggerApp_HiltComponents_SingletonC singletonC,
          ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandle) {
        this.singletonC = singletonC;
        this.activityRetainedCImpl = activityRetainedCImpl;

      }

      private UserRepository userRepository() {
        return new UserRepository(singletonC.baseApiService());
      }

      private PlantRepository plantRepository() {
        return new PlantRepository(singletonC.plantDao());
      }

      private MainViewModel mainViewModel() {
        return new MainViewModel(userRepository(), plantRepository());
      }

      private Provider<MainViewModel> mainViewModelProvider() {
        Object local = mainViewModelProvider;
        if (local == null) {
          local = new SwitchingProvider<>(singletonC, activityRetainedCImpl, viewModelCImpl, 0);
          mainViewModelProvider = (Provider<MainViewModel>) local;
        }
        return (Provider<MainViewModel>) local;
      }

      @Override
      public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
        return Collections.<String, Provider<ViewModel>>singletonMap("com.just.machine.ui.viewmodel.MainViewModel", (Provider) mainViewModelProvider());
      }

      private static final class SwitchingProvider<T> implements Provider<T> {
        private final DaggerApp_HiltComponents_SingletonC singletonC;

        private final ActivityRetainedCImpl activityRetainedCImpl;

        private final ViewModelCImpl viewModelCImpl;

        private final int id;

        SwitchingProvider(DaggerApp_HiltComponents_SingletonC singletonC,
            ActivityRetainedCImpl activityRetainedCImpl, ViewModelCImpl viewModelCImpl, int id) {
          this.singletonC = singletonC;
          this.activityRetainedCImpl = activityRetainedCImpl;
          this.viewModelCImpl = viewModelCImpl;
          this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T get() {
          switch (id) {
            case 0: // com.just.machine.ui.viewmodel.MainViewModel 
            return (T) viewModelCImpl.mainViewModel();

            default: throw new AssertionError(id);
          }
        }
      }
    }
  }

  private static final class ServiceCBuilder implements App_HiltComponents.ServiceC.Builder {
    private final DaggerApp_HiltComponents_SingletonC singletonC;

    private Service service;

    private ServiceCBuilder(DaggerApp_HiltComponents_SingletonC singletonC) {
      this.singletonC = singletonC;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public App_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonC, service);
    }
  }

  private static final class ServiceCImpl extends App_HiltComponents.ServiceC {
    private final DaggerApp_HiltComponents_SingletonC singletonC;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(DaggerApp_HiltComponents_SingletonC singletonC, Service service) {
      this.singletonC = singletonC;

    }
  }
}
