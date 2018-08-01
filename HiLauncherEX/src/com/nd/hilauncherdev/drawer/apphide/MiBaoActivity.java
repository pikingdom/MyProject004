package com.nd.hilauncherdev.drawer.apphide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.android.newline.launcher.R;
import com.nd.hilauncherdev.basecontent.HiActivity;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.data.DrawerPreference;
import com.nd.hilauncherdev.framework.view.commonview.MyphoneContainer;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TabContainerUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 密保问题主页面<br/>
 * author: dingdj<br/>
 * date: 2014/10/15<br/>
 */
public class MiBaoActivity extends HiActivity {

//    public static final String TAG = MiBaoActivity.class.getSimpleName();

    public static final int MIBAO_CREATE_STATE = 0;      //处于新建状态
    public static final int MIBAO_VALIDATE_STATE = 1;    //处于验证状态
    public static final int MIBAO_MODIFY_STATE = 2;      //处于修改状态

    public static final String MIBAO_STATE = "mibao_state";

    private MyphoneContainer container;
    MiBaoSpinner spinner_one;
    MiBaoSpinner spinner_two;
    MiBaoSpinner spinner_three;

    EditText answer_one;
    EditText answer_two;
    EditText answer_three;

    @SuppressWarnings("all")
    TextView tv_mibao_tip;

    private MiBao miBao;

    private int state = MIBAO_CREATE_STATE;

    List<String> answers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabContainerUtil.fullscreen(this);
        container = new MyphoneContainer(this);
        setContentView(container);
        state = getIntent().getIntExtra(MIBAO_STATE, MIBAO_CREATE_STATE);
        init();
    }

    @SuppressWarnings("all")
    private void init() {
        View view = getLayoutInflater().inflate(R.layout.set_mibao_questions, null);

        tv_mibao_tip = (TextView)view.findViewById(R.id.tv_mibao_tip);

        if (state == MIBAO_CREATE_STATE) {
            tv_mibao_tip.setVisibility(View.VISIBLE);
            container.initContainer(this.getString(R.string.set_mibao_questions), view, MyphoneContainer.DEFALUT_THEME);
        } else if (state == MIBAO_VALIDATE_STATE) {
            tv_mibao_tip.setVisibility(View.GONE);
            container.initContainer(this.getString(R.string.validate_mibao_questions), view, MyphoneContainer.DEFALUT_THEME);
        } else if (state == MIBAO_MODIFY_STATE) {
            tv_mibao_tip.setVisibility(View.VISIBLE);
            container.initContainer(this.getString(R.string.modify_mibao_questions), view, MyphoneContainer.DEFALUT_THEME);
        }
        container.setGoBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        spinner_one = (MiBaoSpinner) findViewById(R.id.questions_one);
        spinner_two = (MiBaoSpinner) findViewById(R.id.questions_two);
        spinner_three = (MiBaoSpinner) findViewById(R.id.questions_three);

        answer_one = (EditText) findViewById(R.id.answer_one);
        answer_two = (EditText) findViewById(R.id.answer_two);
        answer_three = (EditText) findViewById(R.id.answer_three);

        if (state == MIBAO_CREATE_STATE) {
            enterCreateMibao();
        } else if (state == MIBAO_VALIDATE_STATE) {
            enterValidateMibao();
        } else if (state == MIBAO_MODIFY_STATE) {
            enterModifyMibao();
        }

        miBao = new MiBao(this);

        initSpinners();
        setListenter();

    }

    private void enterModifyMibao() {
        container.initBottom(new String[]{
                        getString(R.string.common_button_complate)},
                new int[]{-1},
                new View.OnClickListener[]{
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateSuccess()) {
                                    //保存密保内容到preference
                                    try {
                                        saveMibao2Sp();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    finish();
                                    Toast.makeText(MiBaoActivity.this, MiBaoActivity.this.getString(R.string.modify_mibao_success),
                                            Toast.LENGTH_SHORT).
                                            show();
                                }
                            }
                        }});
    }

    private void enterValidateMibao() {
        container.initBottom(new String[]{
                        getString(R.string.reset_pwd)},
                new int[]{-1},
                new View.OnClickListener[]{
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateSuccess()) {
                                    if (answer_one.getText().toString().equals(answers.get(0))
                                            && answer_two.getText().toString().equals(answers.get(1))
                                            && answer_three.getText().toString().equals(answers.get(2))) {
                                        Intent intent = new Intent();


                                        intent.putExtra(AppHideActivity.FROMLAUNCHER, !(Global.getLauncher().getDrawer() != null &&
                                                Global.getLauncher().getDrawer().isVisible()));
                                        intent.putExtra(AppHideEncriptSettingActivity.SHOWANIMATION, true);
                                        intent.setClass(MiBaoActivity.this, AppHideEncriptSettingActivity.class);
                                        SystemUtil.startActivitySafely(MiBaoActivity.this, intent);
                                        finish();
                                    } else {
                                        String str = "";
                                        if (!answer_one.getText().toString().equals(answers.get(0))) {
                                            str = str + getString(R.string.mibao_answer_one);
                                        }
                                        if (!answer_two.getText().toString().equals(answers.get(1))) {
                                            str = str + getString(R.string.mibao_answer_two);
                                        }
                                        if (!answer_three.getText().toString().equals(answers.get(2))) {
                                            str = str + getString(R.string.mibao_answer_three);
                                        }
                                        str += getString(R.string.mibao_un_correct);
                                        Toast.makeText(MiBaoActivity.this, String.format(getString(R.string.mibao_answer_uncorrect), str),
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(MiBaoActivity.this, String.format(getString(R.string.mibao_answer_uncorrect), getString(R.string.answer_un_null)),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }});
    }

    private void enterCreateMibao() {
        container.initBottom(new String[]{
                        getString(R.string.common_button_complate)},
                new int[]{-1},
                new View.OnClickListener[]{
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateSuccess()) {
                                    //保存密保内容到preference
                                    try {
                                        saveMibao2Sp();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    if(getIntent() != null && getIntent().getBooleanExtra("fromPreference", false)){
                                        finish();
                                        return;
                                    }
                                    Intent intent = new Intent();
                                    intent.putExtra(AppHideActivity.SHOWGESTURETIP, true);
                                    intent.setClass(MiBaoActivity.this, AppHideActivity.class);
                                    SystemUtil.startActivitySafely(MiBaoActivity.this, intent);
                                    finish();
                                }
                            }
                        }});
    }

    @SuppressWarnings("all")
    private void saveMibao2Sp() throws UnsupportedEncodingException {
        List<String> list = miBao.getMibaoQuestionList();
        int index = list.indexOf(spinner_one.getSelectedItem());
        StringBuilder buffer = new StringBuilder();
        buffer.append(index);
        buffer.append("|").append(URLEncoder.encode(answer_one.getText().toString(), "utf-8"));

        buffer.append("|").append(list.indexOf(spinner_two.getSelectedItem()));
        buffer.append("|").append(URLEncoder.encode(answer_two.getText().toString(), "utf-8"));

        buffer.append("|").append(list.indexOf(spinner_three.getSelectedItem()));
        buffer.append("|").append(URLEncoder.encode(answer_three.getText().toString(), "utf-8"));
        DrawerPreference.getInstance().setMibaoQuestions(buffer.toString());
    }

    private void setListenter() {
        setSpinnerTouchListener(spinner_one, spinner_two, spinner_three);
        setSpinnerTouchListener(spinner_two, spinner_one, spinner_three);
        setSpinnerTouchListener(spinner_three, spinner_one, spinner_two);
    }

    @SuppressWarnings("all")
    private void setSpinnerTouchListener(final Spinner target, final Spinner... spinners) {
        target.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //如果处于验证状态 不能选择问题
                if(state == MIBAO_VALIDATE_STATE) return true;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    List<String> list = miBao.getMibaoQuestionList();
                    for (Spinner spinner : spinners) {
                        list.remove(spinner.getSelectedItem());
                    }

                    String curSelectQuestion = (String) target.getSelectedItem();
                    MiBaoAdapter adapter = (MiBaoAdapter) target.getAdapter();
                    adapter.setList(list);
                    target.setAdapter(adapter);
                    target.setSelection(list.indexOf(curSelectQuestion));
                }
                return false;
            }
        });
    }

    private void initSpinners() {
        if (state == MIBAO_CREATE_STATE) {
            initSpinner(spinner_one, 1, 1);
            initSpinner(spinner_two, 0, 1);
            initSpinner(spinner_three, 0, 0);
        } else if (state == MIBAO_VALIDATE_STATE) {
            try {
                setMibaoQuestions();
                spinner_one.setShowArrow(false);
                spinner_two.setShowArrow(false);
                spinner_three.setShowArrow(false);
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        } else if (state == MIBAO_MODIFY_STATE) {
            try {
                setMibaoQuestions();
                setMibaoAnswers();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    private void setMibaoAnswers() {
        answer_one.setText(answers.get(0));
        answer_two.setText(answers.get(1));
        answer_three.setText(answers.get(2));
    }

    private void setMibaoQuestions() {
        List<Integer> indexs = new ArrayList<Integer>();
        answers.clear();
        try {
            String questions = DrawerPreference.getInstance().getMibaoQuestions();
            String[] strs = questions.split("\\|");

            for(int i = 0; i<strs.length; i++) {
                if(i%2 == 0) {
                    indexs.add(Integer.parseInt(strs[i]));
                }else{
                    answers.add(URLDecoder.decode(strs[i], "utf-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.finish();
        }
        List<String> list = miBao.getMibaoQuestionList();
        initSpinner(spinner_one, list.get(indexs.get(0)), list.get(indexs.get(1)), list.get(indexs.get(2)));
        initSpinner(spinner_two, list.get(indexs.get(1)), list.get(indexs.get(0)), list.get(indexs.get(2)));
        initSpinner(spinner_three, list.get(indexs.get(2)), list.get(indexs.get(0)), list.get(indexs.get(1)));
    }


    private void initSpinner(Spinner target, int... indexs) {
        List<String> list = miBao.getMibaoQuestionList();
        for (int index : indexs) {
            list.remove(index);
        }

        MiBaoAdapter adapter = new MiBaoAdapter(list, this.getLayoutInflater());
        target.setAdapter(adapter);
    }


    private void initSpinner(Spinner target,String selectQuestion, String... questions) {
        List<String> list = miBao.getMibaoQuestionList();
        for (String question : questions) {
            list.remove(question);
        }

        int index = list.indexOf(selectQuestion);
        MiBaoAdapter adapter = new MiBaoAdapter(list, this.getLayoutInflater());
        target.setAdapter(adapter);
        target.setSelection(index);
    }


    @SuppressWarnings("all")
    private boolean validateSuccess() {
        if (StringUtil.isEmpty(answer_one.getText())
                || StringUtil.isEmpty(answer_two.getText())
                || StringUtil.isEmpty(answer_three.getText())) {
            Toast.makeText(this, getString(R.string.mibao_tip), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
