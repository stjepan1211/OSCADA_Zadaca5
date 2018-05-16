package ada.osc.taskie.util;

import android.app.Application;


import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskGenerator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class TaskieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("taskie.realm")
                .schemaVersion(0)
                //TODO check how realm migrations work
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmList<Task> _tasksList = new RealmList<>();
                        _tasksList.addAll(TaskGenerator.generate(6));
                        realm.insert(_tasksList);
                    }
                })
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
