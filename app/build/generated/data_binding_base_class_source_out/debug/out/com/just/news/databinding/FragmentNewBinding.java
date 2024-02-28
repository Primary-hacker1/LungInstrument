// Generated by data binding compiler. Do not edit!
package com.just.news.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.just.machine.ui.viewmodel.MainViewModel;
import com.just.news.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentNewBinding extends ViewDataBinding {
  @NonNull
  public final AppCompatButton btnMe;

  @NonNull
  public final Button buttonConnect;

  @NonNull
  public final Button buttonDisconnect;

  @NonNull
  public final Button buttonSend;

  @NonNull
  public final EditText editTextTextSend;

  @NonNull
  public final ScrollView scrollView2;

  @NonNull
  public final TextView textViewConnectInfo;

  @NonNull
  public final TextView textViewReceiced;

  @NonNull
  public final ViewToolbarBinding toolbar;

  @Bindable
  protected MainViewModel mVm;

  protected FragmentNewBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppCompatButton btnMe, Button buttonConnect, Button buttonDisconnect, Button buttonSend,
      EditText editTextTextSend, ScrollView scrollView2, TextView textViewConnectInfo,
      TextView textViewReceiced, ViewToolbarBinding toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnMe = btnMe;
    this.buttonConnect = buttonConnect;
    this.buttonDisconnect = buttonDisconnect;
    this.buttonSend = buttonSend;
    this.editTextTextSend = editTextTextSend;
    this.scrollView2 = scrollView2;
    this.textViewConnectInfo = textViewConnectInfo;
    this.textViewReceiced = textViewReceiced;
    this.toolbar = toolbar;
  }

  public abstract void setVm(@Nullable MainViewModel vm);

  @Nullable
  public MainViewModel getVm() {
    return mVm;
  }

  @NonNull
  public static FragmentNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_new, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentNewBinding>inflateInternal(inflater, R.layout.fragment_new, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentNewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_new, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentNewBinding>inflateInternal(inflater, R.layout.fragment_new, null, false, component);
  }

  public static FragmentNewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentNewBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentNewBinding)bind(component, view, R.layout.fragment_new);
  }
}
