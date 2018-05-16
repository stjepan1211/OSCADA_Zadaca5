package ada.osc.taskie.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCategoryActivity extends AppCompatActivity {

    @BindView(R.id.edittext_newcategory_name) EditText mCategoryName;
    @BindView(R.id.imagebutton_newcategory_savecategory) ImageButton mSaveCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.imagebutton_newcategory_savecategory)
    public void saveTask(){
        String name = mCategoryName.getText().toString();

        Category newCategory = new Category(name);
        Intent saveCategoryIntent = new Intent(this, TasksActivity.class);
        saveCategoryIntent.putExtra(TasksActivity.EXTRA_CATEGORY, newCategory);
        setResult(RESULT_OK, saveCategoryIntent);
        finish();
    }
}
