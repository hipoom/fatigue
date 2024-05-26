package com.hipoom.fatigue;

import java.io.File;

import com.hipoom.fatigue.database.DataStore;

/**
 * 疲劳度控制。
 *
 * @author ZhengHaiPeng
 * @since 2024/5/18 11:52
 */
public class Fatigue {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 设置数据保存的地址。
     */
    public static void init(String dataStoreDirectory) {
        DataStore.workspace = new File(dataStoreDirectory);
    }

    /**
     * @param business 功能、业务名称。不能为 null。
     */
    public static Business of(String business) {
        return new Business(business);
    }

}
