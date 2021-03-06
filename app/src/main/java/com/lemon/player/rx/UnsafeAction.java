package com.lemon.player.rx;

import io.reactivex.functions.Action;

/**
 * An Action which does not throw on error.
 */
public interface UnsafeAction extends Action {

    @Override
    void run();
}