package org.totogames.infoengine.util;

/**
 * Represents a method with return type and 4 parameters
 */
@FunctionalInterface
public interface Func4<TResult, TParam1, TParam2, TParam3, TParam4> {
    TResult run(TParam1 param1, TParam2 param2, TParam3 param3, TParam4 param4);
}
