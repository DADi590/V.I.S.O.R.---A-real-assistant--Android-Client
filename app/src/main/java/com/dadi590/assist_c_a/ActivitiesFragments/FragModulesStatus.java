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

package com.dadi590.assist_c_a.ActivitiesFragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dadi590.assist_c_a.GlobalInterfaces.IModule;
import com.dadi590.assist_c_a.ModulesList;
import com.dadi590.assist_c_a.R;

/**
 * <p>Fragment that shows the status of each module of the assistant.</p>
 */
public class FragModulesStatus extends Fragment {

	View current_view = null;

	String color_accent;
	String color_primary;

	@Nullable
	@Override
	public final View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
								   @Nullable final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_modules_status, container, false);
	}

	@Override
	public final void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		current_view = view;

		color_primary = "#" + Integer.toHexString(ContextCompat.getColor(requireActivity(),
				R.color.colorPrimary));
		color_accent = "#" + Integer.toHexString(ContextCompat.getColor(requireActivity(),
				R.color.colorAccent));

		final LinearLayout linearLayout = current_view.findViewById(R.id.frag_modules_status_linear_layout);

		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// Below, convert DP to PX to input on setMargins(), which takes pixels only.
		// 15 DP seems to be enough as margins.
		final Resources resources = requireActivity().getResources();
		final int padding_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15.0F,
				resources.getDisplayMetrics());

		for (int i = 0; i < ModulesList.modules_list_length; ++i) { // Add a switch for each module.
			final SwitchCompat switchCompat = new SwitchCompat(requireContext());
			switchCompat.setId(i); // Set the ID to be the index of the module in the list
			switchCompat.setText((CharSequence) ModulesList.getModuleValue(i, ModulesList.MODULE_NAME)); // Name the switch the module name.
			//switchCompat.setEnabled(true);
			switchCompat.setEnabled(false);
			switchCompat.setLayoutParams(layoutParams);
			switchCompat.setTypeface(null, Typeface.BOLD);
			switchCompat.setTextSize(20.0F);
			switchCompat.setPadding(padding_px, padding_px, padding_px, padding_px);
			switchCompat.setTextIsSelectable(true);
			switchCompat.setBackgroundColor(Color.WHITE);
			switchCompat.setChecked(false);

			if (!(boolean) ModulesList.getModuleValue(i, ModulesList.MODULE_SUPPORTED)) {
				switchCompat.setEnabled(false);
				switchCompat.setTextColor(Color.parseColor(color_primary));
				switchCompat.setBackgroundColor(Color.GRAY);
			}
			// This below was supposed to be in the else statement of the module supported part, but leave it separated.
			// In case something was done wrong in the Modules Manager, the modules may start even if they're not
			// supported, as just happened. This way I can still see there's something wrong (gray background with
			// green letters is not supposed to happen).
			final boolean module_running = ModulesList.isModuleRunning(i);
			switchCompat.setChecked(module_running);
			if (module_running) {
				// If the module is fully working, color the text green (Accent Color), else with orange (holo_orange_dark).
				switchCompat.setTextColor(ModulesList.isModuleFullyWorking(i) ? Color.parseColor(color_accent) :
						Color.parseColor("#FFFF8800"));
			} else {
				// If it's not running, color it red (Primary Color).
				switchCompat.setTextColor(Color.parseColor(color_primary));
			}

			linearLayout.addView(switchCompat);
		}

		// Thread disabled temporarily. Put it stopping after the user leaves this fragment.
		//infinity_checker.start();
	}

	private final Thread infinity_checker = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) { // Keep checking the modules' status.
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
				for (int i = 0; i < ModulesList.modules_list_length; ++i) {
					final SwitchCompat switchCompat = current_view.findViewById(i);

					final boolean module_running = ModulesList.isModuleRunning(i);
					switchCompat.setChecked(module_running); // --> "Animators may only be run on Looper threads"
					if (module_running) {
						// If the module is running, color the text green (Accent Color).
						switchCompat.setTextColor(Color.parseColor(color_accent));
					} else {
						// If it's not running, color it red (Primary Color).
						switchCompat.setTextColor(Color.parseColor(color_primary));
					}
				}

				try {
					Thread.sleep(1_000L);
				} catch (final InterruptedException ignored) {
					Thread.currentThread().interrupt();

					return;
				}
			}
		}
	});
}
