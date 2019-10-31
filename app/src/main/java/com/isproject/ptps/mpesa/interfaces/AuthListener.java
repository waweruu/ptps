package com.isproject.ptps.mpesa.interfaces;

import com.isproject.ptps.mpesa.utils.Pair;
import com.isproject.ptps.mpesa.Mpesa;

/**
 * Created by miles on 18/11/2017.
 */

public interface AuthListener {
    public void onAuthError(Pair<Integer, String> result);
    public void onAuthSuccess();
}
