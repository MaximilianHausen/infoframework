package org.totogames.infoframework.util;

@FunctionalInterface
public interface Func3<TResult, TParam1, TParam2, TParam3> {
    TResult run(TParam1 param1, TParam2 param2, TParam3 param3);
}
