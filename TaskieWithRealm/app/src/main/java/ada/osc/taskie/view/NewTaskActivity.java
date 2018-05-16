package ada.osc.taskie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.UUID;

import ada.osc.taskie.R;
import ada.osc.taskie.TaskRepository;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskGenerator;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.util.SharedPrefsUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.Realm;

public class NewTaskActivity extends AppCompatActivity {

	private boolean isPredefinedCategory = false;
	TaskRepository mRepository = TaskRepository.getInstance();

	@BindView(R.id.edittext_newtask_title)	EditText mTitleEntry;
	@BindView(R.id.edittext_newtask_description) EditText mDescriptionEntry;
	@BindView(R.id.spinner_newtask_priority) Spinner mPriorityEntry;
	@BindView(R.id.spinner_newtask_category) Spinner mCategoryEntry;
	@BindView(R.id.check_box_setcategory) CheckBox mShowPredefinedCategories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		ButterKnife.bind(this);
		setUpSpinnerSource();
		setCategoryUpSpinnerSource();
	}

	private void setUpSpinnerSource() {
		mPriorityEntry.setAdapter(
				new ArrayAdapter<TaskPriority>(
						this, android.R.layout.simple_list_item_1, TaskPriority.values()
				)
		);

		if(SharedPrefsUtil.getPreferencesField(this, SharedPrefsUtil.PRIORITY) != null){
			String defaultPriority = SharedPrefsUtil.getPreferencesField(this, SharedPrefsUtil.PRIORITY);
			mPriorityEntry.setSelection(TaskPriority.valueOf(defaultPriority).ordinal());
		}
		else {
			mPriorityEntry.setSelection(0);
		}
	}

	private void setCategoryUpSpinnerSource() {
		ArrayList<Category> categoryList = new ArrayList<>(mRepository.getCategories());
		String[] categories = new String[categoryList.size()];
		for (int i = 0; i < categoryList.size(); i++){
			categories[i] = categoryList.get(i).getmName();
		}

		mCategoryEntry.setAdapter(
				new ArrayAdapter<String>(
						this, android.R.layout.simple_list_item_1, categories
				)
		);
	}

	@OnCheckedChanged(R.id.check_box_setcategory)
	public void showHideCategoriesSpinner(boolean show){
		if (show) {
			mCategoryEntry.setVisibility(View.VISIBLE);
			mDescriptionEntry.setVisibility(View.GONE);
			isPredefinedCategory = true;
		}
		else {
			mCategoryEntry.setVisibility(View.GONE);
			mDescriptionEntry.setVisibility(View.VISIBLE);
			isPredefinedCategory = false;
		}
	}

	@OnClick(R.id.imagebutton_newtask_savetask)
	public void saveTask(){
		String title = mTitleEntry.getText().toString();
		String description;
		if(isPredefinedCategory) {
			description = mCategoryEntry.getSelectedItem().toString();
		}
		else {
			description = mDescriptionEntry.getText().toString();
		}
		TaskPriority priority = (TaskPriority) mPriorityEntry.getSelectedItem();

		Task newTask = new Task(title, description, priority);
		Intent saveTaskIntent = new Intent(this, TasksActivity.class);
		saveTaskIntent.putExtra(TasksActivity.EXTRA_TASK, newTask);
		setResult(RESULT_OK, saveTaskIntent);
		finish();
	}
}
