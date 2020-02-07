package com.soluciones.demoKit.Model;

import com.hp.jetadvantage.link.api.device.DeviceAttribute;
import com.hp.jetadvantage.link.api.device.DeviceAttributeBase;
import com.soluciones.demoKit.R;

public enum DeviceInfo {
    // Device information
    MODEL(0, R.string.model_name, DeviceAttribute.DA_SYSTEM_MODELNAME),
    HOSTNAME(1, R.string.host_name, DeviceAttribute.DA_NETWORK_HOSTNAME),
    NETWORK_ADDRESS(2, R.string.network_address, null),
    FIRMWARE_VERSION(3, R.string.firmware_version, DeviceAttribute.DA_SYSTEM_FIRMWARE_VERSION),
    DEVICE_ID(4, R.string.device_id, DeviceAttribute.DA_SYSTEM_DEVICE_ID),
    SERIAL_NUMBER(5, R.string.serial_number, DeviceAttribute.DA_SYSTEM_SERIALNUMBER),
    SYSTEM_LANGUAGE(6, R.string.system_language, DeviceAttribute.DA_SYSTEM_LANGUAGE),
    SYSTEM_LANGUAGE_CAPABILITY(7, R.string.system_language_capability, DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY);

    private DeviceAttributeBase mAttribute;
    private int mItemId;
    private int mTitleId;

    public int getItemId() {
        return mItemId;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public DeviceAttributeBase getAttribute() {
        return mAttribute;
    }

    /**
     * @param itemId    in layout
     * @param titleId   in child layout
     * @param attribute to be used with this item, may be null if multiple attributes are composed or
     *                  items is not about attributes
     */
    DeviceInfo(final int itemId, final int titleId, final DeviceAttributeBase attribute) {
        mItemId = itemId;
        mTitleId = titleId;
        mAttribute = attribute;
    }
}
