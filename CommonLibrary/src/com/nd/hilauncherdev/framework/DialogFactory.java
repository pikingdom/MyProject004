package com.nd.hilauncherdev.framework;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.framework.view.dialog.CommonDialog;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

public class DialogFactory {

	public static CommonDialog getSingleButtonDialog(Context ctx, int titleIcon, String title, String content, String btnString,
			final OnClickListener btnClick, final OnClickListener cancleDialog) {
		return getSingleButtonDialog(ctx, titleIcon, title, content, btnString, btnClick, cancleDialog, R.drawable.launcher_alert_error_top_banner);
	}
	
	public static CommonDialog getSingleButtonDialog(Context ctx, int titleIcon, String title, String content, String btnString,
			final OnClickListener btnClick, final OnClickListener cancleDialog, int topBannerDrawable) {
		final CommonDialog dialog = new CommonDialog(ctx, R.style.Dialog);
		dialog.setContentView(R.layout.single_btn_dialog_layout);
		ViewGroup layout = (ViewGroup) dialog.findViewById(R.id.common_dialog_layout);
		int width = (int) (ScreenUtil.getCurrentScreenWidth(ctx) * 0.9f);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(lp);
		
		((ImageView) layout.findViewById(R.id.common_dialog_top_icon)).setImageDrawable(ctx.getResources().getDrawable(titleIcon));
		((TextView) layout.findViewById(R.id.common_dialog_top_title)).setText(title);
		((TextView) layout.findViewById(R.id.common_dialog_content)).setText(content);
		((TextView) layout.findViewById(R.id.common_dialog_button)).setText(btnString);
		layout.findViewById(R.id.common_dialog_top_layout).setBackgroundResource(topBannerDrawable);
		layout.findViewById(R.id.common_dialog_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnClick.onClick(dialog, -1);
			}
		});
		layout.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancleDialog.onClick(dialog, -1);
			}
		});
		
		return dialog;
	}
	
	public static RelativeLayout getSingleButtonDialogLayout(Context ctx, int width, int titleIcon, String title, String content, String btnString,
			final View.OnClickListener btnClick, final View.OnClickListener cancleDialog, final View.OnClickListener checkboxClick){
		RelativeLayout contentLayout = (RelativeLayout) View.inflate(ctx, R.layout.single_btn_dialog_layout, null);
		ViewGroup layout = (ViewGroup) contentLayout.findViewById(R.id.common_dialog_layout);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(lp);
		
		((ImageView) layout.findViewById(R.id.common_dialog_top_icon)).setImageDrawable(ctx.getResources().getDrawable(titleIcon));
		((TextView) layout.findViewById(R.id.common_dialog_top_title)).setText(title);
		((TextView) layout.findViewById(R.id.common_dialog_content)).setText(content);
		((TextView) layout.findViewById(R.id.common_dialog_button)).setText(btnString);
		layout.findViewById(R.id.common_dialog_button).setOnClickListener(btnClick);
		layout.findViewById(R.id.delete).setOnClickListener(cancleDialog);
		layout.findViewById(R.id.common_dialog_top_layout).setBackgroundResource(R.drawable.launcher_set_default_top_banner);
		if(checkboxClick != null){
			layout.findViewById(R.id.checkbox_not_alert).setVisibility(View.VISIBLE);
			layout.findViewById(R.id.checkbox_not_alert).setOnClickListener(checkboxClick);
		}
		return contentLayout;
	}

	public static Dialog getSingleButtonDialogV8(Context ctx, int titleIcon, String content, String desc, String btnString,
												 final OnClickListener btnClick, final OnClickListener cancleDialog) {
		final Dialog dialog = new Dialog(ctx, R.style.Dialog);
		dialog.setContentView(R.layout.single_btn_dialog_layout_v8);
		ViewGroup layout = (ViewGroup) dialog.findViewById(R.id.common_dialog_layout);
		int width = (int) (ScreenUtil.getCurrentScreenWidth(ctx) * 0.9f);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(lp);
		ImageView imageView = (ImageView) layout.findViewById(R.id.common_dialog_top_icon);
		RelativeLayout.LayoutParams lp_image=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
		if (titleIcon > 0) {
			Drawable drawable = ctx.getResources().getDrawable(titleIcon);
			lp_image.width = width;
			lp_image.height = (int) (drawable.getIntrinsicHeight() * 1f / drawable.getIntrinsicWidth() * width);
			imageView.setLayoutParams(lp_image);
			imageView.setImageDrawable(drawable);
		} else {
			Drawable drawable = ctx.getResources().getDrawable(R.drawable.v8_error_404);
			lp_image.width = width;
			lp_image.height = (int) (drawable.getIntrinsicHeight() * 1f / drawable.getIntrinsicWidth() * width);
			imageView.setLayoutParams(lp_image);
		}
		((TextView) layout.findViewById(R.id.common_dialog_content)).setText(content);
		((TextView) layout.findViewById(R.id.common_dialog_desc)).setText(desc);
		((TextView) layout.findViewById(R.id.common_dialog_button)).setText(btnString);
		layout.findViewById(R.id.common_dialog_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnClick.onClick(dialog, -1);
			}
		});
		if (cancleDialog != null) {
			layout.findViewById(R.id.delete).setVisibility(View.VISIBLE);
			layout.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					cancleDialog.onClick(dialog, -1);
				}
			});
		} else {
			layout.findViewById(R.id.delete).setVisibility(View.INVISIBLE);
		}
		dialog.setCancelable(true);
		return dialog;
	}

	public static Dialog getApplyConfirmDialogV8(Context ctx, int titleIcon, String content, String desc, String positive, final String negative,
												 final OnClickListener positiveClick,  final OnClickListener negativeClick) {
		final Dialog dialog = new Dialog(ctx, R.style.Dialog);
		dialog.setContentView(R.layout.apply_confirm_dialog_layout_v8);
		ViewGroup layout = (ViewGroup) dialog.findViewById(R.id.common_dialog_layout);
		int width = (int) (ScreenUtil.getCurrentScreenWidth(ctx) * 0.9f);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(lp);
		ImageView imageView = (ImageView) layout.findViewById(R.id.common_dialog_top_icon);
		RelativeLayout.LayoutParams lp_image=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
		if (titleIcon > 0) {
			Drawable drawable = ctx.getResources().getDrawable(titleIcon);
			lp_image.width = width;
			lp_image.height = (int) (drawable.getIntrinsicHeight() * 1f / drawable.getIntrinsicWidth() * width);
			imageView.setLayoutParams(lp_image);
			imageView.setImageDrawable(drawable);
		} else {
			Drawable drawable = ctx.getResources().getDrawable(R.drawable.v8_error_404);
			lp_image.width = width;
			lp_image.height = (int) (drawable.getIntrinsicHeight() * 1f / drawable.getIntrinsicWidth() * width);
			imageView.setLayoutParams(lp_image);
		}
		((TextView) layout.findViewById(R.id.common_dialog_content)).setText(content);
		((TextView) layout.findViewById(R.id.common_dialog_desc)).setText(desc);
		final TextView btnOk = ((TextView) layout.findViewById(R.id.common_dialog_button_ok));
		final TextView btnCancel = ((TextView) layout.findViewById(R.id.common_dialog_button_cancel));

		btnOk.setText(positive);
		btnCancel.setText(negative);

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(positiveClick != null){
					positiveClick.onClick(dialog, -1);
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(negativeClick != null){
					negativeClick.onClick(dialog, 0);
				}
			}
		});
		layout.findViewById(R.id.delete).setVisibility(View.INVISIBLE);
		dialog.setCancelable(true);
		return dialog;
	}
}
