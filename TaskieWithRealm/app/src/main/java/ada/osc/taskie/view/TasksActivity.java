package ada.osc.taskie.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.TaskRepository;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.util.SharedPrefsUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends AppCompatActivity {

	private static final String TAG = TasksActivity.class.getSimpleName();
	private static final int REQUEST_NEW_TASK = 10;
	private static final int REQUEST_NEW_CATEGORY = 20;

	public static final String EXTRA_TASK = "task";
	public static final String EXTRA_CATEGORY = "category";
	private boolean mIsAscending = true;

	TaskRepository mRepository = TaskRepository.getInstance();
	TaskAdapter mTaskAdapter;

    public static final int SHARED_PREFERENCES_TAG = 1;

    @BindView(R.id.fab_tasks_addNew) FloatingActionButton mNewTask;
	@BindView(R.id.recycler_tasks) RecyclerView mTasksRecycler;
	@BindView(R.id.button_tasks_sort) Button mButtonSortTasks;
	@BindView(R.id.button_tasks_addCategory) Button mButtonAddCategory;

	TaskClickListener mListener = new TaskClickListener() {
		@Override
		public void onClick(Task task) {
			toastTask(task);
		}

		@Override
		public void onLongClick(Task task) {
			removeTask(task);
			updateTasksDisplay(mRepository.getTasks());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);

		ButterKnife.bind(this);
		setUpRecyclerView();
		updateTasksDisplay(mRepository.getTasks());
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateTasksDisplay(mRepository.getTasks());
	}

	private void setUpRecyclerView() {

		int orientation = LinearLayoutManager.VERTICAL;

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
				this,
				orientation,
				false
		);

		RecyclerView.ItemDecoration decoration =
				new DividerItemDecoration(this, orientation);

		RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

		mTaskAdapter = new TaskAdapter(mListener);

		mTasksRecycler.setLayoutManager(layoutManager);
		mTasksRecycler.addItemDecoration(decoration);
		mTasksRecycler.setItemAnimator(animator);
		mTasksRecycler.setAdapter(mTaskAdapter);
	}

	private void updateTasksDisplay(List<Task> tasks) {
		mTaskAdapter.updateTasks(tasks);
		for (Task t : tasks){
			Log.d(TAG, t.getTitle());
		}
	}

	private void toastTask(Task task) {
		Toast.makeText(
				this,
				task.getTitle() + "\n" + task.getDescription(),
				Toast.LENGTH_SHORT
		).show();
	}

	@OnClick(R.id.fab_tasks_addNew)
	public void startNewTaskActivity(){
		Intent newTask = new Intent();
		newTask.setClass(this, NewTaskActivity.class);
		startActivityForResult(newTask, REQUEST_NEW_TASK);
	}

	@OnClick(R.id.button_tasks_addCategory)
	public void startNewCategoryActivity(){
		Intent newTask = new Intent();
		newTask.setClass(this, NewCategoryActivity.class);
		startActivityForResult(newTask, REQUEST_NEW_CATEGORY);
	}

	@OnClick(R.id.button_tasks_sort)
	public void sort(){
		if(mIsAscending){
			updateTasksDisplay(mRepository.getTasksAscending());
			mIsAscending = false;
		}
		else {
			updateTasksDisplay(mRepository.getTasksDescending());
			mIsAscending = true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == REQUEST_NEW_TASK && resultCode == RESULT_OK) {
			if (data != null && data.hasExtra(EXTRA_TASK)) {
				Task task = (Task) data.getSerializableExtra(EXTRA_TASK);
				mRepository.saveTask(task);
				SharedPrefsUtil.storePreferencesField(getApplicationContext(),
						SharedPrefsUtil.PRIORITY,
						String.valueOf(task.getTaskPriorityEnum()));
				updateTasksDisplay(mRepository.getTasks());
			}
		}
		else if(requestCode == REQUEST_NEW_CATEGORY && resultCode == RESULT_OK) {
			if (data != null && data.hasExtra(EXTRA_CATEGORY)) {
				Category category = (Category) data.getSerializableExtra(EXTRA_CATEGORY);
				mRepository.saveCategory(category);
			}
		}
	}
	private void removeTask(Task task) {
		createDialogForTask(task);
	}

	private void createDialogForTask(final Task task){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Confirm");
		builder.setMessage("Are you sure?");

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				mRepository.removeTask(task);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
