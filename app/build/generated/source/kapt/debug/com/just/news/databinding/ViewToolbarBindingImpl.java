package com.just.news.databinding;
import com.just.news.R;
import com.just.news.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ViewToolbarBindingImpl extends ViewToolbarBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final androidx.appcompat.widget.AppCompatTextView mboundView2;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ViewToolbarBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private ViewToolbarBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.appcompat.widget.AppCompatImageView) bindings[1]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[3]
            );
        this.ivTitleBack.setTag(null);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (androidx.appcompat.widget.AppCompatTextView) bindings[2];
        this.mboundView2.setTag(null);
        this.tvRight.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.setTitleImage == variableId) {
            setSetTitleImage((java.lang.Integer) variable);
        }
        else if (BR.title == variableId) {
            setTitle((java.lang.String) variable);
        }
        else if (BR.setRightOnClick == variableId) {
            setSetRightOnClick((java.lang.Integer) variable);
        }
        else if (BR.titleRight == variableId) {
            setTitleRight((java.lang.String) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setSetTitleImage(@Nullable java.lang.Integer SetTitleImage) {
        this.mSetTitleImage = SetTitleImage;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.setTitleImage);
        super.requestRebind();
    }
    public void setTitle(@Nullable java.lang.String Title) {
        this.mTitle = Title;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.title);
        super.requestRebind();
    }
    public void setSetRightOnClick(@Nullable java.lang.Integer SetRightOnClick) {
        this.mSetRightOnClick = SetRightOnClick;
    }
    public void setTitleRight(@Nullable java.lang.String TitleRight) {
        this.mTitleRight = TitleRight;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.titleRight);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.Integer setTitleImage = mSetTitleImage;
        int androidxDatabindingViewDataBindingSafeUnboxSetTitleImage = 0;
        java.lang.String title = mTitle;
        java.lang.String titleRight = mTitleRight;

        if ((dirtyFlags & 0x11L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(setTitleImage)
                androidxDatabindingViewDataBindingSafeUnboxSetTitleImage = androidx.databinding.ViewDataBinding.safeUnbox(setTitleImage);
        }
        if ((dirtyFlags & 0x12L) != 0) {
        }
        if ((dirtyFlags & 0x18L) != 0) {
        }
        // batch finished
        if ((dirtyFlags & 0x11L) != 0) {
            // api target 1

            com.just.machine.helper.binding.Binding.onImage(this.ivTitleBack, androidxDatabindingViewDataBindingSafeUnboxSetTitleImage);
        }
        if ((dirtyFlags & 0x12L) != 0) {
            // api target 1

            com.just.machine.helper.binding.Binding.onText(this.mboundView2, title);
        }
        if ((dirtyFlags & 0x18L) != 0) {
            // api target 1

            com.just.machine.helper.binding.Binding.onText(this.tvRight, titleRight);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): setTitleImage
        flag 1 (0x2L): title
        flag 2 (0x3L): setRightOnClick
        flag 3 (0x4L): titleRight
        flag 4 (0x5L): null
    flag mapping end*/
    //end
}