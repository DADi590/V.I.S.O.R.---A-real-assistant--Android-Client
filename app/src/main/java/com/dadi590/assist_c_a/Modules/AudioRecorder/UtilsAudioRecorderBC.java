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

package com.dadi590.assist_c_a.Modules.AudioRecorder;

import android.content.Intent;

import com.dadi590.assist_c_a.GlobalUtils.UtilsApp;

/**
 * <p>Functions to call to send information to {@link AudioRecorder}, by using broadcasts.</p>
 */
public final class UtilsAudioRecorderBC {

	/**
	 * <p>Private empty constructor so the class can't be instantiated (utility class).</p>
	 */
	private UtilsAudioRecorderBC() {
	}

	/**
	 * <p>Broadcasts a request - more info on {@link CONSTS_BC_AudioRec#ACTION_RECORD_AUDIO}.</p>
	 *
	 * @param start read the action's documentation
	 * @param audio_source read the action's documentation
	 */
	public static void recordAudio(final boolean start, final int audio_source) {
		final Intent broadcast_intent = new Intent(CONSTS_BC_AudioRec.ACTION_RECORD_AUDIO);
		broadcast_intent.putExtra(CONSTS_BC_AudioRec.EXTRA_RECORD_AUDIO_1, start);
		broadcast_intent.putExtra(CONSTS_BC_AudioRec.EXTRA_RECORD_AUDIO_2, audio_source);

		UtilsApp.sendInternalBroadcast(broadcast_intent);
	}
}
