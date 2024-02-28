package com.just.news;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.just.news.databinding.ActivityLoginBindingImpl;
import com.just.news.databinding.ActivityMainBindingImpl;
import com.just.news.databinding.ActivityOrganizationBindingImpl;
import com.just.news.databinding.ActivitySixMinBindingImpl;
import com.just.news.databinding.ActivitySucceedPrisonBindingImpl;
import com.just.news.databinding.ActivityWebBindingImpl;
import com.just.news.databinding.ActivityWelcomeBindingImpl;
import com.just.news.databinding.FragmelayoutSucceedBindingImpl;
import com.just.news.databinding.FragmentLoginBindingImpl;
import com.just.news.databinding.FragmentMainBindingImpl;
import com.just.news.databinding.FragmentMeBindingImpl;
import com.just.news.databinding.FragmentNewBindingImpl;
import com.just.news.databinding.FragmentSettingBindingImpl;
import com.just.news.databinding.ItemNewBindingImpl;
import com.just.news.databinding.ViewToolbarBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYLOGIN = 1;

  private static final int LAYOUT_ACTIVITYMAIN = 2;

  private static final int LAYOUT_ACTIVITYORGANIZATION = 3;

  private static final int LAYOUT_ACTIVITYSIXMIN = 4;

  private static final int LAYOUT_ACTIVITYSUCCEEDPRISON = 5;

  private static final int LAYOUT_ACTIVITYWEB = 6;

  private static final int LAYOUT_ACTIVITYWELCOME = 7;

  private static final int LAYOUT_FRAGMELAYOUTSUCCEED = 8;

  private static final int LAYOUT_FRAGMENTLOGIN = 9;

  private static final int LAYOUT_FRAGMENTMAIN = 10;

  private static final int LAYOUT_FRAGMENTME = 11;

  private static final int LAYOUT_FRAGMENTNEW = 12;

  private static final int LAYOUT_FRAGMENTSETTING = 13;

  private static final int LAYOUT_ITEMNEW = 14;

  private static final int LAYOUT_VIEWTOOLBAR = 15;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(15);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_login, LAYOUT_ACTIVITYLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_organization, LAYOUT_ACTIVITYORGANIZATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_six_min, LAYOUT_ACTIVITYSIXMIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_succeed_prison, LAYOUT_ACTIVITYSUCCEEDPRISON);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_web, LAYOUT_ACTIVITYWEB);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.activity_welcome, LAYOUT_ACTIVITYWELCOME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.fragmelayout_succeed, LAYOUT_FRAGMELAYOUTSUCCEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.fragment_login, LAYOUT_FRAGMENTLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.fragment_main, LAYOUT_FRAGMENTMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.fragment_me, LAYOUT_FRAGMENTME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.fragment_new, LAYOUT_FRAGMENTNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.fragment_setting, LAYOUT_FRAGMENTSETTING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.item_new, LAYOUT_ITEMNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.just.news.R.layout.view_toolbar, LAYOUT_VIEWTOOLBAR);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYLOGIN: {
          if ("layout/activity_login_0".equals(tag)) {
            return new ActivityLoginBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_login is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYORGANIZATION: {
          if ("layout/activity_organization_0".equals(tag)) {
            return new ActivityOrganizationBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_organization is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSIXMIN: {
          if ("layout/activity_six_min_0".equals(tag)) {
            return new ActivitySixMinBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_six_min is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSUCCEEDPRISON: {
          if ("layout/activity_succeed_prison_0".equals(tag)) {
            return new ActivitySucceedPrisonBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_succeed_prison is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYWEB: {
          if ("layout/activity_web_0".equals(tag)) {
            return new ActivityWebBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_web is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYWELCOME: {
          if ("layout/activity_welcome_0".equals(tag)) {
            return new ActivityWelcomeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_welcome is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMELAYOUTSUCCEED: {
          if ("layout/fragmelayout_succeed_0".equals(tag)) {
            return new FragmelayoutSucceedBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragmelayout_succeed is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTLOGIN: {
          if ("layout/fragment_login_0".equals(tag)) {
            return new FragmentLoginBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_login is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMAIN: {
          if ("layout/fragment_main_0".equals(tag)) {
            return new FragmentMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_main is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTME: {
          if ("layout/fragment_me_0".equals(tag)) {
            return new FragmentMeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_me is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNEW: {
          if ("layout/fragment_new_0".equals(tag)) {
            return new FragmentNewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_new is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTSETTING: {
          if ("layout/fragment_setting_0".equals(tag)) {
            return new FragmentSettingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_setting is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMNEW: {
          if ("layout/item_new_0".equals(tag)) {
            return new ItemNewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_new is invalid. Received: " + tag);
        }
        case  LAYOUT_VIEWTOOLBAR: {
          if ("layout/view_toolbar_0".equals(tag)) {
            return new ViewToolbarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for view_toolbar is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(3);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.common.DataBinderMapperImpl());
    result.add(new com.justsafe.libview.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(7);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "setRightOnClick");
      sKeys.put(2, "setTitleImage");
      sKeys.put(3, "title");
      sKeys.put(4, "titleRight");
      sKeys.put(5, "viewModel");
      sKeys.put(6, "vm");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(15);

    static {
      sKeys.put("layout/activity_login_0", com.just.news.R.layout.activity_login);
      sKeys.put("layout/activity_main_0", com.just.news.R.layout.activity_main);
      sKeys.put("layout/activity_organization_0", com.just.news.R.layout.activity_organization);
      sKeys.put("layout/activity_six_min_0", com.just.news.R.layout.activity_six_min);
      sKeys.put("layout/activity_succeed_prison_0", com.just.news.R.layout.activity_succeed_prison);
      sKeys.put("layout/activity_web_0", com.just.news.R.layout.activity_web);
      sKeys.put("layout/activity_welcome_0", com.just.news.R.layout.activity_welcome);
      sKeys.put("layout/fragmelayout_succeed_0", com.just.news.R.layout.fragmelayout_succeed);
      sKeys.put("layout/fragment_login_0", com.just.news.R.layout.fragment_login);
      sKeys.put("layout/fragment_main_0", com.just.news.R.layout.fragment_main);
      sKeys.put("layout/fragment_me_0", com.just.news.R.layout.fragment_me);
      sKeys.put("layout/fragment_new_0", com.just.news.R.layout.fragment_new);
      sKeys.put("layout/fragment_setting_0", com.just.news.R.layout.fragment_setting);
      sKeys.put("layout/item_new_0", com.just.news.R.layout.item_new);
      sKeys.put("layout/view_toolbar_0", com.just.news.R.layout.view_toolbar);
    }
  }
}
