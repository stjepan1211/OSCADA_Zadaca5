package ada.osc.taskie.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class Category extends RealmObject implements Serializable {

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";

    @Required
    @PrimaryKey
    @NonNull
    @RealmField(name = FIELD_ID)
    private String mId;
    @NonNull
    @RealmField(name = FIELD_NAME)
    private String mName;

    public Category() {
    }

    public Category(@NonNull String mName) {
        this.mId = UUID.randomUUID().toString();
        this.mName = mName;
    }

    public static String getFieldId() {
        return FIELD_ID;
    }

    public static String getFieldName() {
        return FIELD_NAME;
    }

    @NonNull
    public String getmId() {
        return mId;
    }

    public void setmId(@NonNull String mId) {
        this.mId = mId;
    }

    @NonNull
    public String getmName() {
        return mName;
    }

    public void setmName(@NonNull String mName) {
        this.mName = mName;
    }
}
