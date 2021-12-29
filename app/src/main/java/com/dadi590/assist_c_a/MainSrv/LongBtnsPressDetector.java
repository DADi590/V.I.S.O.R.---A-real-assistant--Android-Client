/*
 * Copyright 2021 DADi590
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.dadi590.assist_c_a.MainSrv;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.dadi590.assist_c_a.GlobalUtils.UtilsGeneral;
import com.dadi590.assist_c_a.Modules.AudioRecorder.UtilsAudioRecorderBC;
import com.dadi590.assist_c_a.Modules.SpeechRecognition.UtilsSpeechRecognizersBC;
import com.dadi590.assist_c_a.ValuesStorage.CONSTS;
import com.dadi590.assist_c_a.ValuesStorage.ValuesStorage;
import com.dadi590.assist_c_a.R;

/**
 * <p>Class to instantiate to detect long power and home buttons presses and act accordingly.</p>
 */
public final class LongBtnsPressDetector {

	/**
	 * <p>Private empty constructor so the class can't be instantiated (utility class).</p>
	 */
	private LongBtnsPressDetector() {
	}

	// todo This is a module, right...? You have to instantiate it... --> Put it as a module on the app...

	// todo If the overlay permission is granted with the app started, this won't care --> fix it. Put it in a loop or
	//  whatever. Or with some event that the app could broadcast when it detects granted or denied permissions (this
	//  could be useful...).



	public static final int DETECTION_ACTIVATED = 0;
	public static final int UNSUPPORTED_OS_VERSION = 1;
	public static final int UNSUPPORTED_HARDWARE = 2;
	public static final int PERMISSION_DENIED = 3;
	/**
	 * <p>This activates the detection of a long power and home buttons press.</p>
	 * <br>
	 * <p><u>---CONSTANTS---</u></p>
	 * <p>- {@link #DETECTION_ACTIVATED} --> for the returning value: the detection was activated successfully</p>
	 * <p>- {@link #UNSUPPORTED_OS_VERSION} --> for the returning value: the OS is not in a supported version (API 21
	 * through 27)</p>
	 * <p>- {@link #UNSUPPORTED_HARDWARE} --> for the returning value: the hardware does not seem to support the
	 * detection</p>
	 * <p>- {@link #PERMISSION_DENIED} --> for the returning value: the permission to draw a screen overlay was denied</p>
	 * <p><u>---CONSTANTS---</u></p>
	 *
	 * @return one of the constants
	 */
	public static int startDetector() {

		final int system_build = Build.VERSION.SDK_INT;
		if (!(system_build >= 21 && system_build <= 27)) {
			return UNSUPPORTED_OS_VERSION;
		}

		// ONLY AS OF ANDROID 5.0/Lollipop!!!!!!!!!!!!!!!!!!!!! (SDK 21)

		// DOESN'T WORK AS OF ANDROID 9/Pie!!!!!!!!!!!!!!!! (SDK 28)

		final LinearLayout linearLayout = new LinearLayout(UtilsGeneral.getContext()) {

			//home or recent button
			@Override
			public void onCloseSystemDialogs(final String reason) {
				if ("globalactions".equals(reason)) {
					Boolean is_recording_audio = (Boolean) ValuesStorage.getValue(CONSTS.is_recording_audio);
					if (null == is_recording_audio) {
						is_recording_audio = false;
					}
					if (is_recording_audio) {
						// If it's recording audio, it must be stopped. So stop and start the hotword recognizer.
						UtilsAudioRecorderBC.recordAudio(false, -1);
						UtilsSpeechRecognizersBC.startPocketSphinxRecognition();
					} else {
						// If it's not recording audio, start the commands recognizer.
						UtilsSpeechRecognizersBC.startGoogleRecognition();
					}
				} else if ("homekey".equals(reason)) {
					// Here the recognizers are stopped because the user might be wanting to start Google's recognition
					// from the Google App and the microphone would be in use - so this stops it.
					UtilsSpeechRecognizersBC.stopRecognition();
				}/* else if ("recentapps".equals(reason)) {
                }*/
			}
		};

		linearLayout.setFocusable(true);

		final View view = LayoutInflater.from(UtilsGeneral.getContext()).inflate(R.layout.service_layout, linearLayout);
		final WindowManager windowManager = (WindowManager) UtilsGeneral.getContext().
				getSystemService(android.content.Context.WINDOW_SERVICE);

		if (windowManager == null) {
			return UNSUPPORTED_HARDWARE;
		} else {
			final WindowManager.LayoutParams params;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				params = new WindowManager.LayoutParams(
						0,
						0,
						WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
								| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
								| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
								| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
								| WindowManager.LayoutParams.FLAG_SECURE,
						PixelFormat.TRANSPARENT);
			} else {
				params = new WindowManager.LayoutParams(
						0,
						0,
						WindowManager.LayoutParams.TYPE_PHONE,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
								| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
								| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
								| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
								| WindowManager.LayoutParams.FLAG_SECURE,
						PixelFormat.TRANSPARENT);
			}
			params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
			try {
				windowManager.addView(view, params);
			} catch (final WindowManager.BadTokenException ignored) {
				return PERMISSION_DENIED;
			}
		}

		System.out.println("EEEEEEEEEEEEEEE");

		return DETECTION_ACTIVATED;
	}
}