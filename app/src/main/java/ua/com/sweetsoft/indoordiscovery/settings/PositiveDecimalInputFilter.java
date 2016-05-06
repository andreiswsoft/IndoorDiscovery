package ua.com.sweetsoft.indoordiscovery.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;

import ua.com.sweetsoft.indoordiscovery.common.Logger;

public class PositiveDecimalInputFilter implements InputFilter
{
    private EditTextPreference m_preference;

    public PositiveDecimalInputFilter(EditTextPreference preference)
    {
        m_preference = preference;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
    {
        String text = dest.toString();
        String startText = text.substring(0, dstart);
        String endText = text.substring(dend, text.length());
        text = startText + source.subSequence(start, end).toString() + endText;

        Button button = getPositiveButton();
        if (button != null)
        {
            int value = 0;
            try
            {
                value = Integer.parseInt(text);
            }
            catch (NumberFormatException e)
            {
                Logger.logException(e, "Integer.parseInt(" + text + ")");
            }
            button.setEnabled(value > 0);
        }
        return source;
    }

    private Button getPositiveButton()
    {
        Button button = null;

        Dialog dialog = m_preference.getDialog();
        if (dialog instanceof AlertDialog)
        {
            AlertDialog alertDialog = (AlertDialog) dialog;
            button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        }
        return button;
    }
}
