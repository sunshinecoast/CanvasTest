package database;

import android.provider.BaseColumns;

/**
 * Created by zhaoxiaopo on 2018/5/14.
 */
public class DBValues {

    public DBValues() {
    }

    public static final class ValueColumns implements BaseColumns {
        public static final String TABLE_NAME = "test";
        public static final String COLUMN_NAME_USER_NAME = "name";
        public static final String COLUMN_NAME_USER_AGE = "age";
        public static final String COLUMN_NAME_USER_SEX = "sex";
    }
}
