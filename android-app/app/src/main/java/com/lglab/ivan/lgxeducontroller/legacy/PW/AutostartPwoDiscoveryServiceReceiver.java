/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lglab.ivan.lgxeducontroller.legacy.PW;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This receiver starts the UriBeaconDiscoveryService.
 */
public class AutostartPwoDiscoveryServiceReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Intent.ACTION_DATE_CHANGED:
                    //what you want to do
                    break;
                case Intent.ACTION_BOOT_COMPLETED:
                    Intent newIntent = new Intent(context, ScreenListenerService.class);
                    context.startService(newIntent);
                    break;
            }
        }
    }
}
