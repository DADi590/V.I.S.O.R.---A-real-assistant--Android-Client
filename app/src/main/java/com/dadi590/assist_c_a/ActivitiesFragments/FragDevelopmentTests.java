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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.dadi590.assist_c_a.GlobalUtils.UtilsGeneral;
import com.dadi590.assist_c_a.GlobalUtils.UtilsPermissions;

import java.lang.reflect.Method;

/**
 * Class only for testing purposes on the development fragment.
 */
final class FragDevelopmentTests {

	/**
	 * <p>Private empty constructor so the class can't be instantiated (utility class).</p>
	 */
	private FragDevelopmentTests() {
	}

	/**
	 * <p>Method only for testing purposes.</p>
	 */
	static void for_tests() {
		final Context context = UtilsGeneral.getContext();
		// Prepare to work with the Device Policy Manager
		/*final DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		final ComponentName mDeviceAdmin = new ComponentName(context, Recetor_Administracao_Dispositivo.class);
		if (!mDPM.isAdminActive(mDeviceAdmin)) {
			// Launch the Activity to have the user enable Device Admin
			final Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION");
			context.startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
		}*/

		final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//telephonyManager.setDataEnabled(true);

		//Pensar tamb??m sobre outras defini????es, como a Poupan??a de Energia.

		//Testar isto abaixo.
		try {
			final Class<?> telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
			final Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
			getITelephonyMethod.setAccessible(true);
			final ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyManager);
			if (iTelephony != null) {
				iTelephony.enableDataConnectivity();
			} else {
				System.out.println("JJJJJJJJJJ");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}



		//P??R ESTAS DUAS FORMAS NO EXECUTOR!!!!!!!! P??r a de root como ??ltima op????o.

		final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		connectivityManager.setAirplaneMode(true);

		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				android.provider.Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
						1);
			} else {
				android.provider.Settings.Global.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON,
						1);
			}

			final Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", true);
			//context.sendBroadcast(intent, GL_CONSTS.ASSIST_C_A_RECV_PERM);
		} catch (final RuntimeException e) {
			e.printStackTrace();
			Toast.makeText(context, "Exception occured during Airplane Mode ON", Toast.LENGTH_LONG).show();
		}

		//Copiar_bibliotecas.copyAssets(context.);

		//MainSrv.obter_fala().falar("This is a test. LEGION Systems. DAADi EMMPRESAS, Inc. - Always with you!", MainSrv.SEM_COMANDOS_ADICIONAIS, null, false);
		//MainSrv.enviar_tarefa("mobile data on please");

            /*String[] comandos = {"pm grant com.dadi590.assist_c_a android.permission.INJECT_EVENTS"};
            String saida_comandos = Executor.executar_comando(true, comandos);
            System.out.println("--------------------------");
            System.out.println(saida_comandos);
            System.out.println("--------------------------");*/

		PermissionInfo pi = null;
		try {
			pi = context.getPackageManager().getPermissionInfo("android.permission.REBOOT", PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		final PackageManager pm1 = context.getPackageManager();
		final ApplicationInfo ai;
		try {
			if (pi != null) {
				ai = pm1.getApplicationInfo(pi.packageName, 0);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		final boolean protectionLevel = UtilsPermissions.checkSelfPermission("android.permission.UPDATE_APP_OPS_STATS");

		System.out.println("------------------------");
		try {
			final ApplicationInfo applicationInfo = context.getPackageManager().
					getApplicationInfo(context.getPackageName(), 0);
			System.out.println(applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM);
			System.out.println(applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP);
			System.out.println(applicationInfo.privateFlags & ApplicationInfo.PRIVATE_FLAG_PRIVILEGED);
		} catch (final PackageManager.NameNotFoundException ignored) {}
		System.out.println("-----");
		System.out.println(protectionLevel);
		System.out.println(UtilsPermissions.checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE));
		System.out.println(UtilsPermissions.checkSelfPermission(Manifest.permission.CALL_PRIVILEGED));
		System.out.println(UtilsPermissions.checkSelfPermission(Manifest.permission.REBOOT));
		System.out.println(UtilsPermissions.checkSelfPermission(Manifest.permission.SHUTDOWN));
		System.out.println(UtilsPermissions.checkSelfPermission("android.permission.ACCESS_SUPERUSER"));
		System.out.println(UtilsPermissions.checkSelfPermission(Manifest.permission.NETWORK_SETTINGS));
		System.out.println(UtilsPermissions.checkSelfPermission(Manifest.permission.GRANT_RUNTIME_PERMISSIONS));
		System.out.println("------------------------");

            /*Gestor_camara gestor_camara = MainSrv.obter_gestor_camara();
            Context contexto = MainSrv.obter_contexto_aplicacao();
            gestor_camara.usar_camara(contexto,USO_FOTO,false);*/

            /*Runnable runnable = () -> {
                PowerManager pm = (PowerManager) contexto.getSystemService(Context.POWER_SERVICE);
                if (pm != null) {
                    try {
                        pm.reboot(null);
                    } catch (SecurityException e) {
                        MainSrv.obter_fala().falar("Attention - There was an error obtaining the REBOOT permission.", MainSrv.EXECUTOR_ALGUMA_COISA_DITA, null, false);
                    }
                } else {
                    MainSrv.obter_fala().falar("Attention - There was an error rebooting the phone.", MainSrv.EXECUTOR_ALGUMA_COISA_DITA, null, false);
                }
            };

            //N??O D??!!!!!!!!!!

            MainSrv.obter_fala().falar("Rebooting the phone, sir.", MainSrv.EXECUTOR_ALGUMA_COISA_DITA, runnable, false);*/

		//MainSrv.enviar_tarefa("reboot the phone");

		//MainSrv.obter_gestor_camara().usar_camara(contexto, Gestor_camara.USO_FOTO, false, true);

		//MainSrv.para_testes();
		//para_testes=!para_testes;
            /*if (!MainSrv.obter_a_ouvir()) {
                MainSrv.ouvir();
            }*/
	}
}
