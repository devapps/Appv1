/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.infra.qrys_wallet.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infra.qrys_wallet.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating {@link ParsedNdefMessage}s.
 */
public class NdefMessageParser {

    // Utility class
    private NdefMessageParser() {

    }

    /** Parse an NdefMessage */
    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                Log.d("type of data","in UriRecord");
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                Log.d("type of data","in TextRecord");
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                Log.d("type of data","in SmartPoster");
                elements.add(SmartPoster.parse(record));
            } else {
            	elements.add(new ParsedNdefRecord() {
					@Override
					public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
                        Log.d("type of data","in other format");
				        TextView text = (TextView) inflater.inflate(R.layout.tag_text, parent, false);
                        Log.d("first record",new String(record.getPayload()));
				        text.setText(new String(record.getPayload()));
                        text.setTextColor(Color.parseColor("#000000"));
                        return text;
					}
            		
            	});
            }
        }
        return elements;
    }
}
