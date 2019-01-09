/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adityaarora.liveedgedetection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adityaarora.liveedgedetection.R;

public class CameraActivity extends AppCompatActivity {

    private static String NAME = "NAME";

    public static Intent newIntent(String name, Activity activity) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(NAME, name);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {

            String name;

            if (getIntent().hasExtra(NAME)) {
                name = getIntent().getStringExtra(NAME);
            } else {
                name = "First Name";
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance(name))
                    .commit();
        }
    }

}