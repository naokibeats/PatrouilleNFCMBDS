package com.mbds.material.PatrouilleNFC.Utils.Nfc;

/**
 * Created by Safidimahefa on 05/04/2016.
 */
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.mbds.material.PatrouilleNFC.Utils.Nfc.NFCWriteException.NFCErrorType;
import com.naokistudio.material.PatrouilleNFC.R;

import java.io.IOException;


/**
 * NFC manager.
 *
 *
 */
public class NFCManager {
    NfcAdapter nfcAdapter;
    Activity activity;
    PendingIntent pendingIntent;

    TagReadListener onTagReadListener;
    TagWriteListener onTagWriteListener;
    TagWriteErrorListener onTagWriteErrorListener;

    String writeText = null;


    public NFCManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * Sets the listener to read events
     */
    public void setOnTagReadListener(TagReadListener onTagReadListener) {
        this.onTagReadListener = onTagReadListener;
    }

    /**
     * Sets the listener to write events
     */
    public void setOnTagWriteListener(TagWriteListener onTagWriteListener) {
        this.onTagWriteListener = onTagWriteListener;
    }

    /**
     * Sets the listener to write error events
     */
    public void setOnTagWriteErrorListener(TagWriteErrorListener onTagWriteErrorListener) {
        this.onTagWriteErrorListener = onTagWriteErrorListener;
    }

    /**
     * Indicates that we want to write the given text to the next tag detected
     */
    public void writeText(String writeText) {
        this.writeText = writeText;
    }

    /**
     * Stops a writeText operation
     */
    public void undoWriteText() {
        this.writeText = null;
    }


    public boolean onActivityCreate() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        pendingIntent = PendingIntent.getActivity(activity, 0,
                new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        return nfcAdapter!=null;
    }

    /**
     * To be executed on onResume of the activity
     */
    public void onActivityResume() {
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                String message= activity.getResources().getString(R.string.activatenfc);
                final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this.activity);
                dialogBuilder
                        .withTitle("Erreur NFC")
                        .withTitleColor("#FFFFFF")
                        .withDividerColor("#11000000")
                        .withMessage(message)
                        .withMessageColor("#FFFFFFFF")
                        .withDialogColor("#FFE74C3C")
                        .withIcon(this.activity.getResources().getDrawable(R.mipmap.launcher))
                        .withDuration(700)
                        .withEffect(Effectstype.Shake)
                        .withButton1Text("Activer")
                        .isCancelableOnTouchOutside(false)
                        .setCustomView(R.layout.alert_dialog,activity)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                                    activity.startActivity(intent);
                                    dialogBuilder.cancel();
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    dialogBuilder.cancel();
                                    activity.startActivity(intent);
                                    dialogBuilder.cancel();
                                }
                            }
                        })
                        .show();

            }
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null);
        }
    }

    /**
     * To be executed on onPause of the activity
     */
    public void onActivityPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(activity);
        }
    }

    /**
     * To be executed on onNewIntent of activity
     * @param intent
     */
    public void onActivityNewIntent(Intent intent) {
        // activity.setIntent(intent);
        if (writeText == null)
            readTagFromIntent(intent);
        else {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            try {
                writeTag(activity, tag, writeText);
                onTagWriteListener.onTagWritten();
            } catch (NFCWriteException exception) {
                onTagWriteErrorListener.onTagWriteError(exception);
            } finally {
                writeText = null;
            }
        }
    }

    /**
     * Reads a tag for a given intent and notifies listeners
     * @param intent
     */
    private void readTagFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag myTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefRecord[] records = ((NdefMessage) rawMsgs[0]).getRecords();
                String text = ndefRecordToString(records[0]);
                onTagReadListener.onTagRead(text);
            }
        }
    }

    public String ndefRecordToString(NdefRecord record) {
        byte[] payload = record.getPayload();
        return new String(payload);
    }

    /**
     * Writes a text to a tag
     * @param context
     * @param tag
     * @param data
     * @throws NFCWriteException
     */
    protected void writeTag(Context context, Tag tag, String data) throws NFCWriteException {
        NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, data.getBytes());


        NdefMessage message = new NdefMessage(new NdefRecord[] { relayRecord });

        Ndef ndef = Ndef.get(tag);
        if (ndef != null) {
            try {
                ndef.connect();
            } catch (IOException e) {
                throw new NFCWriteException(NFCWriteException.NFCErrorType.unknownError);
            }
            if (!ndef.isWritable()) {
                throw new NFCWriteException(NFCErrorType.ReadOnly);
            }

            int size = message.toByteArray().length;
            if (ndef.getMaxSize() < size) {
                throw new NFCWriteException(NFCErrorType.NoEnoughSpace);
            }

            try {
                ndef.writeNdefMessage(message);
            } catch (TagLostException tle) {
                throw new NFCWriteException(NFCWriteException.NFCErrorType.tagLost, tle);
            } catch (IOException ioe) {
                throw new NFCWriteException(NFCErrorType.formattingError, ioe);// nfcFormattingErrorTitle
            } catch (FormatException fe) {
                throw new NFCWriteException(NFCErrorType.formattingError, fe);
            }
        } else {
            NdefFormatable format = NdefFormatable.get(tag);
            if (format != null) {
                try {
                    format.connect();
                    format.format(message);
                } catch (TagLostException tle) {
                    throw new NFCWriteException(NFCErrorType.tagLost, tle);
                } catch (IOException ioe) {
                    throw new NFCWriteException(NFCErrorType.formattingError, ioe);
                } catch (FormatException fe) {
                    throw new NFCWriteException(NFCErrorType.formattingError, fe);
                }
            } else {
                throw new NFCWriteException(NFCErrorType.noNdefError);
            }
        }

    }

    public interface TagReadListener {
        public void onTagRead(String tagRead);
    }

    public interface TagWriteListener {
        public void onTagWritten();
    }

    public interface TagWriteErrorListener {
        public void onTagWriteError(NFCWriteException exception);
    }

}
