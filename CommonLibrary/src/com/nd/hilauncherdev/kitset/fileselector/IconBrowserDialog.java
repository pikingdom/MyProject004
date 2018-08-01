package com.nd.hilauncherdev.kitset.fileselector;

import java.io.File;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.StringUtil;

/**
 * 图标浏览对话框
 */
public class IconBrowserDialog extends AlertDialog implements OnClickListener, OnItemClickListener, OnItemSelectedListener {
	
    private ImageButton uplevelButton;

    private TextView pathView;

    private IconListAdapter mFilelistAdapter;

    private List<SDCardFile> mSDCardFiles = new ArrayList<SDCardFile>();

    private IconFileLoader mFileLoader;

    private boolean mFileLoaded;

    private IconFileController fileController = IconFileController.getInstance();

    private Context mContext;

    private ListView fileListView;

    private String loadPath = "/";

    private View browserView;
    
    //private ImageView preview;
    
    private FileClickCallback clickCallbak = null;;
    private FileFilter mPreFilter = null;
   // private ShowMonitor monitor;
    
	public IconBrowserDialog(Context context) {
        this(context, context.getResources().getString(R.string.default_set_controller_select_pic), Environment.getExternalStorageDirectory().getAbsolutePath(),null);

    }
	  public IconBrowserDialog(Context context,FileFilter fileFilter) {
	        this(context, context.getResources().getString(R.string.file_browser_dialog_file_select), Environment.getExternalStorageDirectory().getAbsolutePath(),fileFilter);
	    }
	  
    public IconBrowserDialog(Context context, String title, String loadPath,FileFilter fileFilter) {
        super(context);
        mContext = context;
        mPreFilter =fileFilter;
        if(StringUtil.isEmpty(loadPath)){
        	loadPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        this.loadPath = loadPath;
        if(!new File(this.loadPath).exists()){
        	this.loadPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        setInverseBackgroundForced(true);
        init(title);
      //  monitor = new ShowMonitor();
       // monitor.start();
    }

    private void init(String title) {
        browserView = getLayoutInflater().inflate(R.layout.file_browser_icon, null);
        this.setView(browserView);
        setTitle(title);
 
        fileController.setCurrentDirPath(this.loadPath);
        loadFile((Activity) mContext);

       // preview = (ImageView)browserView.findViewById(R.id.preview);
        fileListView = (ListView) browserView.findViewById(R.id.fileBrowser);
        fileListView.setAdapter(mFilelistAdapter);
        fileListView.setOnItemClickListener(this);
        fileListView.setOnItemSelectedListener(this);
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
            mFilelistAdapter = new IconListAdapter(launcher, R.layout.file_browser_list_item_icon_text, mSDCardFiles);
        }
        if (mFileLoader != null && mFileLoader.isRunning()) {
            mFileLoader.stop();
        }
        mFileLoaded = false;
        if (null == mFileLoader) {
            mFileLoader = new IconFileLoader(launcher, mFilelistAdapter, pathView, mFileLoaded);
        }
        
        // 载入跟目录。
        mFileLoader.startFileLoader(loadPath,mPreFilter);
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
    	else{
    		pathView.setText(nowPath);
    	}
    	if((Environment.getExternalStorageDirectory()+"/|icons|").equals(nowPath)){
    		fileController.clickFile(new SDCardFile(SDCardFile.ICON_FILE_DIR), null);
    	}
    }

    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获取点击的文件。
		SDCardFile file = (SDCardFile) parent.getItemAtPosition(position);
		// fileListView.requestFocusFromTouch();
		// 打开文件
		if (!file.isNULLFile() && !file.getFile().isDirectory()) {
			try {
				String filePath = file.getFile().getAbsolutePath();

				if (clickCallbak != null) {
					clickCallbak.onClickFile(view, filePath);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			cancel();
		}
		else if(file.type == SDCardFile.ICON_FILE){
			
			if (clickCallbak != null) {
				clickCallbak.onClickFile(view, "" + file.getTag());
			}
			cancel();
		}
		fileController.clickFile(file, mPreFilter);
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void setClickCallbak(FileClickCallback clickCallbak) {
		this.clickCallbak = clickCallbak;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
}
