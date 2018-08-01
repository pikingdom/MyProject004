package com.nd.weather.widget.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.weather.widget.R;

public class CommonDialog extends Dialog {

	public CommonDialog(Context context, int theme) {
		super(context, theme);
	}

	public CommonDialog(Context context) {
		super(context);
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Context context;

		private Drawable icon;

		private CharSequence title;

		private CharSequence message;

		private CharSequence positiveButtonText;

		private CharSequence negativeButtonText;

		private View contentView;

		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(CharSequence message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(CharSequence title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the Dialog icon from resource
		 * 
		 * @param icon
		 * @return
		 */
		public Builder setIcon(int icon) {
			this.icon = context.getResources().getDrawable(icon);
			return this;
		}

		/**
		 * Set the Dialog icon from Drawable
		 * 
		 * @param title
		 * @return
		 */
		public Builder setIcon(Drawable icon) {
			this.icon = icon;
			return this;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 * 
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(CharSequence positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(CharSequence negativeButtonText,	DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Create the custom dialog
		 */
		public CommonDialog create() {
			// instantiate the dialog with the custom Theme
			final CommonDialog dialog = new CommonDialog(context, R.style.Dialog);
			dialog.setContentView(R.layout.weather_common_dialog_layout);
			ViewGroup layout = (ViewGroup) dialog.findViewById(R.id.common_dialog_layout);
			
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			int width = (int) (dm.widthPixels * 0.9f);
			
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
			layout.setLayoutParams(lp);

			// set the dialog title
			if (icon != null) {
				((ImageView) layout.findViewById(R.id.common_dialog_top_icon)).setImageDrawable(icon);
			} else {
				layout.findViewById(R.id.common_dialog_top_icon).setVisibility(View.GONE);
			}
			((TextView) layout.findViewById(R.id.common_dialog_top_title)).setText(title);

			// set the confirm button
			if (positiveButtonText != null) {
				
				((Button) layout.findViewById(R.id.positive_button)).setText(positiveButtonText);

				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positive_button)).setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positive_button).setVisibility(View.GONE);
			}

			// set the cancel button
			if (negativeButtonText != null) {

				((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);

				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negative_button)).setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog,	DialogInterface.BUTTON_NEGATIVE);
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negative_button).setVisibility(View.GONE);
				layout.findViewById(R.id.separator).setVisibility(View.GONE);
			}

			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.common_dialog_content)).setText(message);
			} else{
				((TextView) layout.findViewById(R.id.common_dialog_content)).setVisibility(View.GONE);
			}
			
			if (contentView != null) {
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.common_dialog_custom_view_layout)).addView(contentView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			} else {
				layout.findViewById(R.id.common_dialog_custom_view_layout).setVisibility(View.GONE);
			}

			return dialog;

		}

	}
}
