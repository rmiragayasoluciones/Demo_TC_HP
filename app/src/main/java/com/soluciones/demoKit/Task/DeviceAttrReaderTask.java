package com.soluciones.demoKit.Task;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.device.DeviceAttribute;
import com.hp.jetadvantage.link.api.device.DeviceService;
import com.soluciones.demoKit.Model.DeviceInfo;
import com.soluciones.demoKit.R;
import com.soluciones.demoKit.ReporteDeErrores;

import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class DeviceAttrReaderTask extends AsyncTask<Void, Object, Map<DeviceInfo, String>> {

    private final WeakReference<ReporteDeErrores> mContextRef;

    public DeviceAttrReaderTask(final ReporteDeErrores context) {
        mContextRef = new WeakReference<>(context);
    }


    private static final EnumSet<DeviceInfo> sItemsToRefresh = EnumSet.of(
            DeviceInfo.MODEL,
            DeviceInfo.HOSTNAME,
            DeviceInfo.NETWORK_ADDRESS,
            DeviceInfo.FIRMWARE_VERSION,
            DeviceInfo.DEVICE_ID,
            DeviceInfo.SERIAL_NUMBER,
            DeviceInfo.SYSTEM_LANGUAGE,
            DeviceInfo.SYSTEM_LANGUAGE_CAPABILITY
    );

    @Override
    protected Map<DeviceInfo, String> doInBackground(Void... voids) {
        final Resources res = mContextRef.get().getResources();
        Map<DeviceInfo, String> values = new HashMap<>();

        String VALUE_NA = res.getString(R.string.na);

        if (sItemsToRefresh.contains(DeviceInfo.HOSTNAME)) {
            Result result = new Result();
            String hostName = DeviceService.getString(mContextRef.get(), DeviceInfo.HOSTNAME.getAttribute(), result);

            if (result.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_NETWORK_HOSTNAME", result);
                hostName = VALUE_NA;
            }

            values.put(DeviceInfo.HOSTNAME, hostName);
        }

        if (sItemsToRefresh.contains(DeviceInfo.MODEL)) {
            Result vendorResult = new Result();
            String vendor = DeviceService.getString(mContextRef.get(), DeviceAttribute.DA_DEVICE_VENDOR, vendorResult);

            if (vendorResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_DEVICE_VENDOR", vendorResult);
                vendor = VALUE_NA;
            }

            Result modelNameResult = new Result();
            String modelName = DeviceService.getString(mContextRef.get(), DeviceInfo.MODEL.getAttribute(), modelNameResult);

            if (modelNameResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_SYSTEM_MODELNAME", modelNameResult);
                modelName = VALUE_NA;
            }

            String value = res.getString(R.string.model_value, modelName, vendor);

            values.put(DeviceInfo.MODEL, value);
        }

        if (sItemsToRefresh.contains(DeviceInfo.FIRMWARE_VERSION)) {
            Result result = new Result();
            String firmwareVersion = DeviceService.getString(mContextRef.get(), DeviceInfo.FIRMWARE_VERSION.getAttribute(), result);

            if (result.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_FIRMWARE_VERSION", result);
                firmwareVersion = VALUE_NA;
            }

            values.put(DeviceInfo.FIRMWARE_VERSION, firmwareVersion);
        }

        if (sItemsToRefresh.contains(DeviceInfo.DEVICE_ID)) {
            Result deviceIdResult = new Result();
            String deviceId = DeviceService.getString(mContextRef.get(), DeviceInfo.DEVICE_ID.getAttribute(), deviceIdResult);

            if (deviceIdResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_SYSTEM_DEVICE_ID", deviceIdResult);
                deviceId = VALUE_NA;
            }

            Result productNumberResult = new Result();
            String productNumber = DeviceService.getString(mContextRef.get(), DeviceAttribute.DA_SYSTEM_PRODUCT_NUMBER, productNumberResult);

            if (productNumberResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_SYSTEM_PRODUCT_NUMBER", productNumberResult);
                productNumber = VALUE_NA;
            }

            String value = res.getString(R.string.device_id_value, deviceId, productNumber);
            values.put(DeviceInfo.DEVICE_ID, value);
        }

        if (sItemsToRefresh.contains(DeviceInfo.SERIAL_NUMBER)) {
            Result serialNumberResult = new Result();
            String serialNumber = DeviceService.getString(mContextRef.get(), DeviceInfo.SERIAL_NUMBER.getAttribute(), serialNumberResult);

            if (serialNumberResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_SYSTEM_SERIALNUMBER", serialNumberResult);
                serialNumber = VALUE_NA;
            }

            Result formatterSNResult = new Result();
            String formatterSerialNumber = DeviceService.getString(mContextRef.get(), DeviceAttribute.DA_SYSTEM_FORMATTER_SERIAL_NUMBER, formatterSNResult);

            if (formatterSNResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_SYSTEM_FORMATTER_SERIAL_NUMBER", formatterSNResult);
                formatterSerialNumber = VALUE_NA;
            }

            String value = res.getString(R.string.serial_number_value, serialNumber, formatterSerialNumber);
            values.put(DeviceInfo.SERIAL_NUMBER, value);
        }

        if (sItemsToRefresh.contains(DeviceInfo.SYSTEM_LANGUAGE)) {
            Result result = new Result();
            String systemLanguage = DeviceService.getString(mContextRef.get(), DeviceInfo.SYSTEM_LANGUAGE.getAttribute(), result);

            if (result.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_SYSTEM_LANGUAGE", result);
                systemLanguage = VALUE_NA;
            }

            values.put(DeviceInfo.SYSTEM_LANGUAGE, systemLanguage);
        }

        if (sItemsToRefresh.contains(DeviceInfo.SYSTEM_LANGUAGE_CAPABILITY)) {
            Result result = new Result();
            String[] systemLanguageCapability = DeviceService.getStringArray(mContextRef.get(), DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY, result);
            StringBuilder systemLanguages = new StringBuilder(VALUE_NA);
            if (result.getCode() != RESULT_OK || systemLanguageCapability == null) {
                publishProgress("DeviceService.getStringArray DA_FEATURE_SYSTEM_LANGUAGE_CAPABILITY", result);
            } else {
                systemLanguages = new StringBuilder();
                for (String language : systemLanguageCapability) {
                    systemLanguages.append(language).append(",");
                }
            }
            values.put(DeviceInfo.SYSTEM_LANGUAGE_CAPABILITY, systemLanguages.toString());
        }

        if (sItemsToRefresh.contains(DeviceInfo.NETWORK_ADDRESS)) {
            Result macAddressResult = new Result();
            String macAddress = DeviceService.getString(mContextRef.get(), DeviceAttribute.DA_NETWORK_MACADDRESS, macAddressResult);

            if (macAddressResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_NETWORK_MACADDRESS", macAddressResult);
                macAddress = VALUE_NA;
            }

            Result ipAddressResult = new Result();
            String ipAddress = DeviceService.getString(mContextRef.get(), DeviceAttribute.DA_NETWORK_IPADDRESS, ipAddressResult);

            if (ipAddressResult.getCode() != RESULT_OK) {
                publishProgress("DeviceService.getString DA_NETWORK_IPADDRESS", ipAddressResult);
                ipAddress = VALUE_NA;
            }

            String value = res.getString(R.string.address_value, ipAddress, macAddress);

            values.put(DeviceInfo.NETWORK_ADDRESS, value);
        }

        return values;
    }

    @Override
    protected void onPostExecute(Map<DeviceInfo, String> result) {
        super.onPostExecute(result);
        if (result != null) {
            mContextRef.get().handleUpdate(result);
            return;
        }
        mContextRef.get().handleException(new Exception("Error de Servicio"));
    }


}
