package ada.osc.taskie.persistance;

import java.util.List;
import java.util.UUID;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class Database {

	private Realm mRealm;

	public Database(){
		mRealm = Realm.getDefaultInstance();
	}

	public List<Task> getTasks(){
		//TODO check/ask how to do this async in all 'get' methods, goal is to use findAllAsync()
		return mRealm.where(Task.class).findAll();
	}

	public List<Task> getTasksAscending() {
		//TODO check why column name is not same as @RealmField in Task model
		//TODO now sorting is done with strings (HIGH, MEDIUM, LOW), consider to store integers in
		//db instead of strings
		return mRealm.where(Task.class).findAll().sort("mPriority", Sort.ASCENDING);
	}

	public List<Task> getTasksDescending() {
		return mRealm.where(Task.class).findAll().sort("mPriority", Sort.ASCENDING);
	}

	public void save(Task task){
		mRealm.beginTransaction();
		Task newTask = mRealm.createObject(Task.class, UUID.randomUUID().toString());
		newTask.setTitle(task.getTitle());
		newTask.setDescription(task.getDescription());
		newTask.setTaskPriorityEnum(task.getTaskPriorityEnum());
		mRealm.commitTransaction();
	}

	public void delete(final Task task) {
		mRealm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				RealmResults<Task> rows = realm.where(Task.class)
						.equalTo(Task.FIELD_ID,task.getId())
						.findAll();
				rows.deleteAllFromRealm();
			}
		});
	}

	public void saveCategory(Category category) {
		mRealm.beginTransaction();
		Category newCategory = mRealm.createObject(Category.class, category.getmId());
		newCategory.setmName(category.getmName());
		mRealm.commitTransaction();
	}

	public List<Category> getCategories(){
		return mRealm.where(Category.class).findAll();
	}
}
