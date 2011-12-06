/*
 * Copyright (C) 2008 ZXing authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jwetherell.quick_response_code.result;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.jwetherell.quick_response_code.core.Result;
import com.jwetherell.quick_response_code.core.result.ParsedResult;
import com.jwetherell.quick_response_code.core.result.ParsedResultType;


/**
 * A base class for the Android-specific barcode handlers. These allow the app to polymorphically
 * suggest the appropriate actions for each data type.
 * 
 * This class also contains a bunch of utility methods to take common actions like opening a URL.
 * They could easily be moved into a helper object, but it can't be static because the Activity
 * instance is needed to launch an intent.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public abstract class ResultHandler {
    private static final DateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        // For dates without a time, for purposes of interacting with Android, the resulting
        // timestamp needs to be midnight of that day in GMT. See:
        // http://code.google.com/p/android/issues/detail?id=8330
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private final ParsedResult result;
    private final Activity activity;
    private final Result rawResult;

    public ResultHandler(Activity activity, ParsedResult result) {
        this(activity, result, null);
    }

    public ResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        this.result = result;
        this.activity = activity;
        this.rawResult = rawResult;
    }

    public ParsedResult getResult() {
        return result;
    }

    public Activity getActivity() {
        return activity;
    }

    public Result getRawResult() {
        return rawResult;
    }

    /**
     * Create a possibly styled string for the contents of the current barcode.
     * 
     * @return The text to be displayed.
     */
    public CharSequence getDisplayContents() {
        String contents = result.getDisplayResult();
        return contents.replace("\r", "");
    }

    /**
     * A string describing the kind of barcode that was found, e.g. "Found contact info".
     * 
     * @return The resource ID of the string.
     */
    public abstract int getDisplayTitle();

    /**
     * A convenience method to get the parsed type. Should not be overridden.
     * 
     * @return The parsed type, e.g. URI or ISBN
     */
    public ParsedResultType getType() {
        return result.getType();
    }
}
