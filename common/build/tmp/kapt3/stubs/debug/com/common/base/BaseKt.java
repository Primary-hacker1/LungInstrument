package com.common.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 2, d1 = {"\u0000\u0086\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a8\u0010\u0014\u001a\u00020\u0015\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020\u00172\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00172\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001aH\u0086\b\u00f8\u0001\u0000\u001a(\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0010\u001a\u0014\u0010#\u001a\u00020\u001b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001b0%\u001a\u000e\u0010&\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020\u0010\u001a$\u0010(\u001a\u0010\u0012\f\u0012\n **\u0004\u0018\u0001H\u0016H\u00160)\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160)\u001a\u0012\u0010+\u001a\u00020\u0010*\u00020\u00102\u0006\u0010,\u001a\u00020\u0010\u001a\u001e\u0010-\u001a\u0002H\u0016\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u00020/H\u0086\b\u00a2\u0006\u0002\u00100\u001a4\u0010-\u001a\u0002H\u0016\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u0002012\n\b\u0002\u00102\u001a\u0004\u0018\u0001032\b\b\u0002\u00104\u001a\u00020\u0015H\u0086\b\u00a2\u0006\u0002\u00105\u001a\u001e\u0010-\u001a\u0002H\u0016\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u000206H\u0086\b\u00a2\u0006\u0002\u00107\u001a(\u0010-\u001a\u0002H\u0016\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u0002032\b\b\u0002\u00104\u001a\u00020\u0015H\u0086\b\u00a2\u0006\u0002\u00108\u001a\u001e\u0010-\u001a\u0002H\u0016\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u000209H\u0086\b\u00a2\u0006\u0002\u0010:\u001a\u0012\u0010;\u001a\u00020\u0010*\u00020\u00102\u0006\u0010<\u001a\u00020\r\u001a\u0012\u0010;\u001a\u00020\u0010*\u00020\u00102\u0006\u0010=\u001a\u00020\u0010\u001a\u0014\u0010>\u001a\u00020\u0015*\u00020?2\b\b\u0002\u0010@\u001a\u00020\u0001\u001a\u0014\u0010>\u001a\u00020\u0015*\u00020A2\b\b\u0002\u0010@\u001a\u00020\u0001\u001a\n\u0010B\u001a\u00020\u001b*\u00020/\u001a\n\u0010B\u001a\u00020\u001b*\u00020C\u001a\\\u0010D\u001a\b\u0012\u0004\u0012\u0002H\u00160E\"\u0006\b\u0000\u0010\u0016\u0018\u0001*\u0002H\u00162\u0006\u0010D\u001a\u00020\u00012\u0014\b\n\u0010F\u001a\u000e\u0012\u0004\u0012\u00020G\u0012\u0004\u0012\u00020\u001b0\u001a2\u0019\b\b\u0010\u0019\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010I\u001aT\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00160E\"\u0006\b\u0000\u0010\u0016\u0018\u0001*\u0002H\u00162\u0014\b\n\u0010F\u001a\u000e\u0012\u0004\u0012\u00020G\u0012\u0004\u0012\u00020\u001b0\u001a2\u0019\b\b\u0010\u0019\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010K\u001a\u0012\u0010L\u001a\u00020\u0010*\u00020\u00102\u0006\u0010M\u001a\u00020N\u001a\u0012\u0010L\u001a\u00020\u0010*\u00020\u00102\u0006\u0010O\u001a\u000206\u001a\u0012\u0010P\u001a\u00020\u0010*\u00020N2\u0006\u0010Q\u001a\u00020R\u001a\u001a\u0010S\u001a\u00020\u0010*\u00020N2\u0006\u0010Q\u001a\u00020R2\u0006\u0010T\u001a\u00020R\u001a\u0012\u0010U\u001a\u00020R*\u00020N2\u0006\u0010V\u001a\u00020\u0010\u001a \u0010W\u001a\u0004\u0018\u000103*\u0002062\u0012\u0010X\u001a\u000e\u0012\u0004\u0012\u000203\u0012\u0004\u0012\u00020\u00150\u001a\u001a\u0012\u0010Y\u001a\u00020\u0010*\u0002062\u0006\u0010V\u001a\u00020\u0010\u001a\n\u0010Z\u001a\u00020\u001b*\u000206\u001a<\u0010[\u001a\u00020\u001b\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020\u0017*\u0002092\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010N2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001aH\u0086\b\u00f8\u0001\u0000\u001a\n\u0010\\\u001a\u00020\u001b*\u000206\u001a\n\u0010]\u001a\u00020\u0015*\u00020^\u001a\n\u0010_\u001a\u00020\u0015*\u00020^\u001a\n\u0010`\u001a\u00020\u0015*\u00020^\u001a\n\u0010a\u001a\u00020\u0015*\u00020^\u001a\n\u0010b\u001a\u00020\u0015*\u00020^\u001a\u001f\u0010c\u001a\b\u0012\u0004\u0012\u0002H\u00160d\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u00020/H\u0086\b\u001a\u001f\u0010c\u001a\b\u0012\u0004\u0012\u0002H\u00160d\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u000206H\u0086\b\u001a\u001f\u0010c\u001a\b\u0012\u0004\u0012\u0002H\u00160d\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u000209H\u0086\b\u001a2\u0010e\u001a\u00020\u001b\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020\u0017*\u0002H\u00162\u0012\u0010f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170g\"\u00020\u0017H\u0086\b\u00a2\u0006\u0002\u0010h\u001a\u0016\u0010i\u001a\u00020\u001b*\u00020/2\n\u0010j\u001a\u0006\u0012\u0002\b\u00030k\u001a\u001e\u0010i\u001a\u00020\u001b*\u00020/2\n\u0010j\u001a\u0006\u0012\u0002\b\u00030k2\u0006\u0010l\u001a\u00020m\u001a=\u0010n\u001a\u00020\u001b*\u0004\u0018\u00010\u00172!\u0010o\u001a\u001d\u0012\u0013\u0012\u00110\u0017\u00a2\u0006\f\bp\u0012\b\bQ\u0012\u0004\b\b(f\u0012\u0004\u0012\u00020\u001b0\u001a2\f\u0010q\u001a\b\u0012\u0004\u0012\u00020\u001b0%\u001a<\u0010r\u001a\u00020\u001b\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020C*\u0002H\u00162\u0019\b\b\u0010\u0019\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010s\u001a\u001e\u0010t\u001a\u00020\u001b*\u0002062\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u000206\u0012\u0004\u0012\u00020\u001b0\u001a\u001aT\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00160E\"\u0006\b\u0000\u0010\u0016\u0018\u0001*\u0002H\u00162\u0014\b\n\u0010F\u001a\u000e\u0012\u0004\u0012\u00020G\u0012\u0004\u0012\u00020\u001b0\u001a2\u0019\b\b\u0010\u0019\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010K\u001a\n\u0010u\u001a\u00020\u0010*\u00020R\u001a\u001a\u0010v\u001a\u00020\r*\u00020\r2\u0006\u0010w\u001a\u00020\r2\u0006\u0010x\u001a\u00020\r\u001a\u001a\u0010v\u001a\u00020\u0010*\u00020\u00102\u0006\u0010w\u001a\u00020\u00102\u0006\u0010x\u001a\u00020\u0010\u001a7\u0010y\u001a\u00020\u001b*\u0002062\b\b\u0002\u0010@\u001a\u00020\u00012!\u0010z\u001a\u001d\u0012\u0013\u0012\u001106\u00a2\u0006\f\bp\u0012\b\bQ\u0012\u0004\b\b(O\u0012\u0004\u0012\u00020\u001b0\u001a\u001a\u001c\u0010{\u001a\b\u0012\u0004\u0012\u0002H\u00160)\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160)\u001a\u0012\u0010|\u001a\u00020\u0010*\u00020\u00102\u0006\u0010,\u001a\u00020\u0010\u001a5\u0010}\u001a\u00020\u001b\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020/*\u00020N2\u0017\u0010z\u001a\u0013\u0012\u0004\u0012\u00020~\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u001aB\u0010\u007f\u001a\u00020\u001b\"\u0004\b\u0000\u0010\u0016*\t\u0012\u0004\u0012\u0002H\u00160\u0080\u00012\u0013\u0010\u0081\u0001\u001a\u000e\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a2\u0014\u0010\u0082\u0001\u001a\u000f\u0012\u0005\u0012\u00030\u0083\u0001\u0012\u0004\u0012\u00020\u001b0\u001a\u001aD\u0010\u007f\u001a\u00030\u0084\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160)2\u0013\u0010\u0081\u0001\u001a\u000e\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a2\u0014\u0010\u0082\u0001\u001a\u000f\u0012\u0005\u0012\u00030\u0083\u0001\u0012\u0004\u0012\u00020\u001b0\u001aH\u0007\u001aU\u0010\u0085\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00160E\"\u0006\b\u0000\u0010\u0016\u0018\u0001*\u0002H\u00162\u0014\b\n\u0010F\u001a\u000e\u0012\u0004\u0012\u00020G\u0012\u0004\u0012\u00020\u001b0\u001a2\u0019\b\b\u0010\u0019\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010K\u001a$\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u001d*\u0002062\t\b\u0002\u0010\u0087\u0001\u001a\u00020\r2\b\b\u0002\u0010 \u001a\u00020!H\u0007\u001a\'\u0010\u0088\u0001\u001a\u00020\u001b*\u00020/2\t\u0010\u0089\u0001\u001a\u0004\u0018\u00010\u00102\t\b\u0002\u0010\u008a\u0001\u001a\u00020\u0010\u00a2\u0006\u0003\u0010\u008b\u0001\u001a!\u0010\u0088\u0001\u001a\u00020\u001b*\u00020/2\t\u0010\u0089\u0001\u001a\u0004\u0018\u00010R2\t\b\u0002\u0010\u008a\u0001\u001a\u00020\u0010\u001a!\u0010\u0088\u0001\u001a\u00020\u001b*\u0002092\t\u0010\u0089\u0001\u001a\u0004\u0018\u00010R2\t\b\u0002\u0010\u008a\u0001\u001a\u00020\u0010\u001a\u0014\u0010\u008c\u0001\u001a\u00020\u0010*\u00020R2\u0007\u0010\u008d\u0001\u001a\u00020\u0010\u001a\u000b\u0010\u008e\u0001\u001a\u00020\u001b*\u000206\u001a\u000b\u0010\u008f\u0001\u001a\u00020\u001b*\u000206\u001a\u000b\u0010\u0090\u0001\u001a\u00020\u0001*\u00020N\u001a\u000b\u0010\u0091\u0001\u001a\u00020R*\u00020N\u001a\u000b\u0010\u0092\u0001\u001a\u00020\u001b*\u000206\u001aF\u0010\u0093\u0001\u001a\u00020\u001b\"\b\b\u0000\u0010\u0016*\u000206*\u0002H\u00162\u0007\u0010\u0094\u0001\u001a\u00020\u00152\u001a\b\u0002\u0010\u0095\u0001\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0096\u0001\u001a\u0014\u0010\u0093\u0001\u001a\u00020\u001b*\u0002062\u0007\u0010\u0097\u0001\u001a\u00020\u0015\u001aF\u0010\u0098\u0001\u001a\u00020\u001b\"\b\b\u0000\u0010\u0016*\u000206*\u0002H\u00162\u0007\u0010\u0094\u0001\u001a\u00020\u00152\u001a\b\u0002\u0010\u0095\u0001\u001a\u0013\u0012\u0004\u0012\u0002H\u0016\u0012\u0004\u0012\u00020\u001b0\u001a\u00a2\u0006\u0002\bHH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0096\u0001\u001a\u0014\u0010\u0098\u0001\u001a\u00020\u001b*\u0002062\u0007\u0010\u0097\u0001\u001a\u00020\u0015\u001a+\u0010\u0099\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00160d\"\n\b\u0000\u0010\u0016\u0018\u0001*\u00020.*\u0002062\t\b\u0002\u0010\u009a\u0001\u001a\u00020\u0015H\u0086\b\u001a\u0015\u0010\u009b\u0001\u001a\u00020\u001b*\u00020R2\b\u0010\u009c\u0001\u001a\u00030\u009d\u0001\u001a\u0019\u0010\u009e\u0001\u001a\u00020\u0010*\u00020\u00102\f\u0010f\u001a\b\u0012\u0004\u0012\u00020\u00100%\"\u001a\u0010\u0000\u001a\u00020\u0001X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0004\b\u0004\u0010\u0005\"\u001b\u0010\u0006\u001a\u00020\u00078FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t\"\u0015\u0010\f\u001a\u00020\r*\u00020\r8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\"\u0015\u0010\f\u001a\u00020\u0010*\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u0011\"\u0015\u0010\u0012\u001a\u00020\r*\u00020\r8F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u000f\"\u0015\u0010\u0012\u001a\u00020\u0010*\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0011\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u009f\u0001"}, d2 = {"lastClickTime", "", "getLastClickTime", "()J", "setLastClickTime", "(J)V", "mainThread", "Landroid/os/Handler;", "getMainThread", "()Landroid/os/Handler;", "mainThread$delegate", "Lkotlin/Lazy;", "dp2px", "", "getDp2px", "(F)F", "", "(I)I", "sp2px", "getSp2px", "check", "", "T", "", "ctx", "run", "Lkotlin/Function1;", "", "createBitmapSafely", "Landroid/graphics/Bitmap;", "width", "height", "config", "Landroid/graphics/Bitmap$Config;", "retryCount", "onUI", "callback", "Lkotlin/Function0;", "versionThen", "target", "async", "Lio/reactivex/Single;", "kotlin.jvm.PlatformType", "biggerThen", "o", "bind", "Landroidx/viewbinding/ViewBinding;", "Landroid/app/Activity;", "(Landroid/app/Activity;)Landroidx/viewbinding/ViewBinding;", "Landroid/view/LayoutInflater;", "parent", "Landroid/view/ViewGroup;", "attach", "(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Z)Landroidx/viewbinding/ViewBinding;", "Landroid/view/View;", "(Landroid/view/View;)Landroidx/viewbinding/ViewBinding;", "(Landroid/view/ViewGroup;Z)Landroidx/viewbinding/ViewBinding;", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;)Landroidx/viewbinding/ViewBinding;", "changeAlpha", "f", "a", "clickNoRepeat", "Landroidx/appcompat/app/AppCompatActivity;", "interval", "Landroidx/fragment/app/DialogFragment;", "closeBoard", "Landroid/widget/EditText;", "delay", "Lcom/common/base/CommonUtil$Task;", "err", "", "Lkotlin/ExtensionFunctionType;", "(Ljava/lang/Object;JLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Lcom/common/base/CommonUtil$Task;", "doAsync", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Lcom/common/base/CommonUtil$Task;", "findColor", "context", "Landroid/content/Context;", "view", "findDrawableId", "name", "", "findId", "type", "findName", "id", "findRootGroup", "filter", "getColor", "gone", "identityCheck", "invisible", "isCreated", "Landroidx/lifecycle/LifecycleOwner;", "isDestroyed", "isInitialized", "isResumed", "isStarted", "lazyBind", "Lkotlin/Lazy;", "log", "value", "", "(Ljava/lang/Object;[Ljava/lang/Object;)V", "navigateToActivity", "c", "Ljava/lang/Class;", "bundle", "Landroid/os/Bundle;", "notNull", "notNullAction", "Lkotlin/ParameterName;", "nullAction1", "onActionDone", "(Landroid/widget/EditText;Lkotlin/jvm/functions/Function1;)V", "onClick", "parseColor", "range", "min", "max", "setNoRepeatListener", "action", "single", "smallerThen", "startActivity", "Landroid/content/Intent;", "subscribes", "Lcom/uber/autodispose/SingleSubscribeProxy;", "onSuccess", "onError", "Lcom/common/throwe/BaseResponseThrowable;", "Lio/reactivex/disposables/Disposable;", "task", "toBitmap", "scale", "toast", "msg", "duration", "(Landroid/app/Activity;Ljava/lang/Integer;I)V", "tryInt", "def", "tryInvisible", "tryVisible", "versionCode", "versionName", "visible", "visibleOrGone", "boolean", "onVisible", "(Landroid/view/View;ZLkotlin/jvm/functions/Function1;)V", "flag", "visibleOrInvisible", "withThis", "inflate", "writeTo", "file", "Ljava/io/File;", "zeroTo", "common_debug"})
public final class BaseKt {
    
    /**
     * 类似java->runOnUiThread
     */
    @org.jetbrains.annotations.NotNull
    private static final kotlin.Lazy mainThread$delegate = null;
    
    /**
     * 防止重复点击事件 默认0.5秒内不可重复点击
     * @param interval 时间间隔 默认0.5秒
     * @param action 执行方法
     */
    private static long lastClickTime = 0L;
    
    /**
     * create by 2019/12/30
     * 扩展函数基类
     * @author zt
     */
    public static final void toast(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$toast, @org.jetbrains.annotations.Nullable
    java.lang.String msg, int duration) {
    }
    
    public static final void toast(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$toast, @org.jetbrains.annotations.Nullable
    java.lang.Integer msg, int duration) {
    }
    
    public static final void toast(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment $this$toast, @org.jetbrains.annotations.Nullable
    java.lang.String msg, int duration) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final <T extends java.lang.Object>io.reactivex.Single<T> single(@org.jetbrains.annotations.NotNull
    io.reactivex.Single<T> $this$single) {
        return null;
    }
    
    public static final void navigateToActivity(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$navigateToActivity, @org.jetbrains.annotations.NotNull
    java.lang.Class<?> c) {
    }
    
    public static final void navigateToActivity(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$navigateToActivity, @org.jetbrains.annotations.NotNull
    java.lang.Class<?> c, @org.jetbrains.annotations.NotNull
    android.os.Bundle bundle) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final <T extends java.lang.Object>io.reactivex.Single<T> async(@org.jetbrains.annotations.NotNull
    io.reactivex.Single<T> $this$async) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @io.reactivex.annotations.SchedulerSupport(value = "none")
    @io.reactivex.annotations.CheckReturnValue
    public static final <T extends java.lang.Object>io.reactivex.disposables.Disposable subscribes(@org.jetbrains.annotations.NotNull
    io.reactivex.Single<T> $this$subscribes, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.common.throwe.BaseResponseThrowable, kotlin.Unit> onError) {
        return null;
    }
    
    public static final <T extends java.lang.Object>void subscribes(@org.jetbrains.annotations.NotNull
    com.uber.autodispose.SingleSubscribeProxy<T> $this$subscribes, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.common.throwe.BaseResponseThrowable, kotlin.Unit> onError) {
    }
    
    /**
     * 设置view显示
     */
    public static final void visible(@org.jetbrains.annotations.NotNull
    android.view.View $this$visible) {
    }
    
    /**
     * 设置view占位隐藏
     */
    public static final void invisible(@org.jetbrains.annotations.NotNull
    android.view.View $this$invisible) {
    }
    
    /**
     * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
     */
    public static final void visibleOrGone(@org.jetbrains.annotations.NotNull
    android.view.View $this$visibleOrGone, boolean flag) {
    }
    
    /**
     * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
     */
    public static final void visibleOrInvisible(@org.jetbrains.annotations.NotNull
    android.view.View $this$visibleOrInvisible, boolean flag) {
    }
    
    /**
     * 设置view隐藏
     */
    public static final void gone(@org.jetbrains.annotations.NotNull
    android.view.View $this$gone) {
    }
    
    /**
     * 类似java->runOnUiThread
     */
    @org.jetbrains.annotations.NotNull
    public static final android.os.Handler getMainThread() {
        return null;
    }
    
    public static final void onUI(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> callback) {
    }
    
    /**
     * 将view转为bitmap
     */
    @org.jetbrains.annotations.Nullable
    @java.lang.Deprecated
    public static final android.graphics.Bitmap toBitmap(@org.jetbrains.annotations.NotNull
    android.view.View $this$toBitmap, float scale, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap.Config config) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final android.graphics.Bitmap createBitmapSafely(int width, int height, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap.Config config, int retryCount) {
        return null;
    }
    
    public static final long getLastClickTime() {
        return 0L;
    }
    
    public static final void setLastClickTime(long p0) {
    }
    
    public static final void setNoRepeatListener(@org.jetbrains.annotations.NotNull
    android.view.View $this$setNoRepeatListener, long interval, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super android.view.View, kotlin.Unit> action) {
    }
    
    public static final boolean clickNoRepeat(@org.jetbrains.annotations.NotNull
    androidx.appcompat.app.AppCompatActivity $this$clickNoRepeat, long interval) {
        return false;
    }
    
    public static final boolean clickNoRepeat(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.DialogFragment $this$clickNoRepeat, long interval) {
        return false;
    }
    
    public static final void notNull(@org.jetbrains.annotations.Nullable
    java.lang.Object $this$notNull, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<java.lang.Object, kotlin.Unit> notNullAction, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> nullAction1) {
    }
    
    /**
     * 对一个输入框关闭键盘
     */
    public static final void closeBoard(@org.jetbrains.annotations.NotNull
    android.widget.EditText $this$closeBoard) {
    }
    
    /**
     * 对一个activity关闭键盘
     */
    public static final void closeBoard(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$closeBoard) {
    }
    
    /**
     * 对一个颜色值设置它的透明度
     * 只支持#AARRGGBB格式排列的颜色值
     */
    public static final int changeAlpha(int $this$changeAlpha, int a) {
        return 0;
    }
    
    /**
     * 以浮点数的形式，以当前透明度为基础，
     * 调整颜色值的透明度
     */
    public static final int changeAlpha(int $this$changeAlpha, float f) {
        return 0;
    }
    
    public static final float getDp2px(float $this$dp2px) {
        return 0.0F;
    }
    
    public static final float getSp2px(float $this$sp2px) {
        return 0.0F;
    }
    
    public static final int getDp2px(int $this$dp2px) {
        return 0;
    }
    
    public static final int getSp2px(int $this$sp2px) {
        return 0;
    }
    
    /**
     * 将一个整数作为id来寻找对应的颜色值，
     * 如果找不到或者发生了异常，那么将会返回白色
     */
    public static final int findColor(int $this$findColor, @org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return 0;
    }
    
    /**
     * 将一个整数作为id来寻找对应的颜色值，
     * 如果找不到或者发生了异常，那么将会返回白色
     */
    public static final int findColor(int $this$findColor, @org.jetbrains.annotations.NotNull
    android.view.View view) {
        return 0;
    }
    
    /**
     * 如果当前整数是0，那么获取回调函数中的值作为返回值
     * 否则返回当前
     */
    public static final int zeroTo(int $this$zeroTo, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<java.lang.Integer> value) {
        return 0;
    }
    
    /**
     * 对一个浮点数做范围约束
     */
    public static final float range(float $this$range, float min, float max) {
        return 0.0F;
    }
    
    /**
     * 将一个字符串转换为颜色值
     * 只接受1～8位0～F之间的字符
     */
    public static final int parseColor(@org.jetbrains.annotations.NotNull
    java.lang.String $this$parseColor) {
        return 0;
    }
    
    /**
     * 尝试将一个字符串转换为整形，
     * 如果发生了异常或者为空，那么将会返回默认值
     */
    public static final int tryInt(@org.jetbrains.annotations.NotNull
    java.lang.String $this$tryInt, int def) {
        return 0;
    }
    
    /**
     * 以一个View为res来源获取指定id的颜色值
     */
    public static final int getColor(@org.jetbrains.annotations.NotNull
    android.view.View $this$getColor, int id) {
        return 0;
    }
    
    /**
     * 一个整形的范围约束
     */
    public static final int range(int $this$range, int min, int max) {
        return 0;
    }
    
    /**
     * 从Context中尝试通过名字获取一个drawable的id
     */
    public static final int findDrawableId(@org.jetbrains.annotations.NotNull
    android.content.Context $this$findDrawableId, @org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return 0;
    }
    
    /**
     * 从Context中尝试通过名字获取一个指定类型的资源id
     */
    public static final int findId(@org.jetbrains.annotations.NotNull
    android.content.Context $this$findId, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String type) {
        return 0;
    }
    
    /**
     * 尝试通过一个id获取对应的资源名
     */
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String findName(@org.jetbrains.annotations.NotNull
    android.content.Context $this$findName, int id) {
        return null;
    }
    
    /**
     * 从context中获取当前应用的版本名称
     */
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String versionName(@org.jetbrains.annotations.NotNull
    android.content.Context $this$versionName) {
        return null;
    }
    
    /**
     * 从context中获取当前应用的版本名称
     */
    public static final long versionCode(@org.jetbrains.annotations.NotNull
    android.content.Context $this$versionCode) {
        return 0L;
    }
    
    /**
     * 将一段文本写入一个文件中
     * 它属于IO操作，这是一个耗时的任务，
     * 需要在子线程中执行
     */
    public static final void writeTo(@org.jetbrains.annotations.NotNull
    java.lang.String $this$writeTo, @org.jetbrains.annotations.NotNull
    java.io.File file) {
    }
    
    public static final <T extends android.view.View>void visibleOrGone(@org.jetbrains.annotations.NotNull
    T $this$visibleOrGone, boolean p1_32355860, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onVisible) {
    }
    
    public static final <T extends android.view.View>void visibleOrInvisible(@org.jetbrains.annotations.NotNull
    T $this$visibleOrInvisible, boolean p1_32355860, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onVisible) {
    }
    
    public static final void tryVisible(@org.jetbrains.annotations.NotNull
    android.view.View $this$tryVisible) {
    }
    
    public static final void tryInvisible(@org.jetbrains.annotations.NotNull
    android.view.View $this$tryInvisible) {
    }
    
    public static final void onClick(@org.jetbrains.annotations.NotNull
    android.view.View $this$onClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super android.view.View, kotlin.Unit> callback) {
    }
    
    public static final int smallerThen(int $this$smallerThen, int o) {
        return 0;
    }
    
    public static final int biggerThen(int $this$biggerThen, int o) {
        return 0;
    }
    
    public static final boolean versionThen(int target) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final android.view.ViewGroup findRootGroup(@org.jetbrains.annotations.NotNull
    android.view.View $this$findRootGroup, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super android.view.ViewGroup, java.lang.Boolean> filter) {
        return null;
    }
    
    /**
     * 是否已经被销毁
     */
    public static final boolean isDestroyed(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner $this$isDestroyed) {
        return false;
    }
    
    /**
     * 是否已经被创建
     */
    public static final boolean isCreated(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner $this$isCreated) {
        return false;
    }
    
    /**
     * 是否已经被初始化
     */
    public static final boolean isInitialized(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner $this$isInitialized) {
        return false;
    }
    
    /**
     * 是否已经开始运行
     */
    public static final boolean isStarted(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner $this$isStarted) {
        return false;
    }
    
    /**
     * 是否已经可见
     */
    public static final boolean isResumed(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner $this$isResumed) {
        return false;
    }
}