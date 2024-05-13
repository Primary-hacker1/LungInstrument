package com.just.machine.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
    private boolean isOnWrited = false;

    Context context = null;
    String pathName = "";

    public PdfDocumentAdapter(Context ctxt, String pathName) {
        context = ctxt;
        this.pathName = pathName;
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
        } else {
            PrintDocumentInfo.Builder builder =
                    new PrintDocumentInfo.Builder(" file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
            layoutResultCallback.onLayoutFinished(builder.build(),
                    !printAttributes1.equals(printAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File file = new File(pathName);
            in = new FileInputStream(file);
            out = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            byte[] buf = new byte[16384];
            int size;

            while ((size = in.read(buf)) >= 0
                    && !cancellationSignal.isCanceled()) {
                out.write(buf, 0, size);
            }

            if (cancellationSignal.isCanceled()) {
                writeResultCallback.onWriteCancelled();
            } else {
                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        } catch (Exception e) {
            writeResultCallback.onWriteFailed(e.getMessage());
            Log.e("PdfDocumentAdapter ", e + "");
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                Log.e("PdfDocumentAdapter ", e + "");

            }
        }
    }


    /**
     * 打印报告预览结束
     */
    @Override
    public void onFinish() {
        super.onFinish();
        Log.e("PdfDocumentAdapter","TAG" + "PdfDocumentAdapter=====里面======onFinish==" + isOnWrited);
        if (mOnPrintStatueListener != null) {
            mOnPrintStatueListener.onPrintStatue(isOnWrited);
        }

    }
    /**
     * 打印报告开始预览
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.e("PdfDocumentAdapter ", "开始打印");

    }

    private OnPrintStatueListener mOnPrintStatueListener;

    public void setOnPrintStatue(OnPrintStatueListener OnPrintStatueListener) {
        this.mOnPrintStatueListener = OnPrintStatueListener;
    }

    public interface OnPrintStatueListener {
        void onPrintStatue(boolean statue);
    }
}