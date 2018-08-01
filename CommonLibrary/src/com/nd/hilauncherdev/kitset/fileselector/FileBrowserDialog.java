package com.nd.hilauncherdev.kitset.fileselector;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nd.android.pandahome2.R;

/**
 * 文件浏览对话框
 */
public class FileBrowserDialog extends AlertDialog implements OnClickListener, OnItemClickListener , OnItemLongClickListener{

    // private FileGridView fileGridView;

    // private static final int DEFAULT_APPLICATIONS_NUMBER = 42;
    private ImageButton uplevelButton;

    private TextView pathView;

    //
    // private FileAdapter mFileAdapter;
    //	

    private FileListAdapter mFilelistAdapter;

    private List<SDCardFile> mSDCardFiles = new ArrayList<SDCardFile>();

    private FileLoader mFileLoader;

    private boolean mFileLoaded;

    private FileController fileController = FileController.getInstance();

    private Context mContext;

    private Activity mActivity;
    
    private ListView fileListView;

    private String loadPath = "/";

    View browserView;

    private FileFilter mPreFilter = null;

    public FileBrowserDialog(Context context, FileFilter fileFilter) {
        this(context, context.getResources().getString(R.string.tab1_select_file), Environment.getExternalStorageDirectory().getAbsolutePath(), fileFilter);
    }

/*    public void setPreFilter(FileFilter preFilter) {
        mPreFilter = preFilter;
    }*/

    public FileBrowserDialog(Context context, String title, String loadPath, FileFilter fileFilter) {
        super(context);
        mPreFilter = fileFilter;
        mContext = context;
        this.loadPath = loadPath;
        setInverseBackgroundForced(true);
        init(title);
    }
    
    public FileBrowserDialog(Context context, Activity activity,  String title, String loadPath, FileFilter fileFilter) {
        super(context);
        mPreFilter = fileFilter;
        mContext = context;
        mActivity = activity;
        this.loadPath = loadPath;
        setInverseBackgroundForced(true);
        init(title);
    }

    protected void init(String title) {
        browserView = getLayoutInflater().inflate(R.layout.file_browser_list, null);
        this.setView(browserView);
        setTitle(title);
        fileController.setCurrentDirPath(this.loadPath);
        loadFile((Activity) mContext);

        fileListView = (ListView) browserView.findViewById(R.id.fileBrowser);
        fileListView.setAdapter(mFilelistAdapter);        
        fileListView.setOnItemClickListener(this);
        fileListView.setOnItemLongClickListener(this);
        // pathView = (TextView) browserView.findViewById(R.id.currentPath);
        uplevelButton = (ImageButton) browserView.findViewById(R.id.uplevel);
        uplevelButton.setOnClickListener(this);

    }

    /**
     * 载入文件
     * @param launcher
     */
    public void loadFile(Activity launcher) {
        pathView = (TextView) browserView.findViewById(R.id.currentPath);
        pathView.setText(this.loadPath);
        if (mFileLoader == null) {
            mFilelistAdapter = new FileListAdapter(launcher, R.layout.file_browser_list_item_icon_text, mSDCardFiles);
        }
        if (mFileLoader != null && mFileLoader.isRunning()) {
            mFileLoader.stop();
        }
        mFileLoaded = false;
        if (null == mFileLoader) {
            mFileLoader = new FileLoader(launcher, mFilelistAdapter, pathView, mFileLoaded);
        }

        // 载入跟目录。
        mFileLoader.startFileLoader(loadPath, mPreFilter);
    }

    private String lastPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String nowPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    @Override
    public void onClick(View v) {
    	lastPath = fileController.getCurrentDirPath();
    	nowPath = fileController.returnParent(mPreFilter);
    	if(nowPath.equals(lastPath)){
    		this.dismiss();
    	}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 获取点击的文件。
        SDCardFile file = (SDCardFile) parent.getItemAtPosition(position);
        // fileListView.requestFocusFromTouch();
        // 打开文件
        if (!file.isNULLFile() && !file.getFile().isDirectory()) {
            try {
            	Context context;
            	if( mActivity != null ) 
            		context = mActivity;
            	else
            		context = mContext;
                if (context instanceof FileClickCallback) {
                    String filePath = file.getFile().getAbsolutePath();
                    // PandaTheme pandaTheme =
                    // ThemeLoader.getInstance().loaderThemeZip(zipPath);
                    ((FileClickCallback) context).onClickFile(view, filePath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            cancel();
        }
        fileController.clickFile(file, mPreFilter).toString();
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
        SDCardFile file = (SDCardFile) parent.getItemAtPosition(position);
        // fileListView.requestFocusFromTouch();
        // 打开文件
        if (!file.isNULLFile() && file.getFile().isDirectory()) {
            try {
            	Context context;
            	if( mActivity != null ) 
            		context = mActivity;
            	else
            		context = mContext;
            	
                if ( context instanceof FileLongClickCallback) {
                    String filePath = file.getFile().getAbsolutePath();
                    ((FileLongClickCallback) context).onLongClickFile(view, filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cancel();
        }
       // fileController.clickFile(file, mPreFilter).toString();
		return false;
	}
}
