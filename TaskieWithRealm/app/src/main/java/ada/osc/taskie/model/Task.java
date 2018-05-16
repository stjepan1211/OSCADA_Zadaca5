package ada.osc.taskie.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class Task extends RealmObject implements Serializable{

	public static final String FIELD_ID = "id";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_COMPLETED = "completed";
	public static final String FIELD_PRIORITY = "priority";

	private static int sID = 0;
	
	@Required
	@PrimaryKey
	@NonNull
	@RealmField(name = FIELD_ID)
	private String mId;
	@NonNull
	@RealmField(name = FIELD_TITLE)
	private String mTitle;
	@NonNull
	@RealmField(name = FIELD_DESCRIPTION)
	private String mDescription;
	@NonNull
	@RealmField(name = FIELD_COMPLETED)
	private boolean mCompleted;
	@NonNull
	@RealmField(name = FIELD_PRIORITY)
	private String mPriority;

	public Task() {
	}

	public Task(String title, String description, TaskPriority priority) {
		mId = UUID.randomUUID().toString();
		mTitle = title;
		mDescription = description;
		mCompleted = false;
		mPriority = convertTaskPriorityEnumToString(priority);
	}

	public String getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public void setCompleted(boolean completed) {
		mCompleted = completed;
	}

	public void setTaskPriorityEnum(TaskPriority taskPriority) {
		this.mPriority = taskPriority.toString();
	}

	public TaskPriority getTaskPriorityEnum() {
		return TaskPriority.valueOf(mPriority);
	}

	public String convertTaskPriorityEnumToString(TaskPriority taskPriority) {
		return String.valueOf(taskPriority.toString());
	}
}
