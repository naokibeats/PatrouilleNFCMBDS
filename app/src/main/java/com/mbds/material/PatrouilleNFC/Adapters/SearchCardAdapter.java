package com.mbds.material.PatrouilleNFC.Adapters;

/**
 * Created by Safidimahefa on 17/04/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mbds.material.PatrouilleNFC.Model.Personne;
import com.mbds.material.PatrouilleNFC.ProfilDetailActivity;
import com.naokistudio.material.PatrouilleNFC.R;
import java.util.ArrayList;
import java.util.List;

public class SearchCardAdapter  extends ArrayAdapter<Personne>  {
    private static final String TAG = "SearchCardApdapter";
    private List<Personne> personneList = new ArrayList<Personne>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }

    public SearchCardAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Personne object) {
        personneList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.personneList.size();
    }

    @Override
    public Personne getItem(int index) {
        return this.personneList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_view, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        final Personne personne = getItem(position);
        viewHolder.line1.setText(personne.getNom() + " " + personne.getPrenom());
        viewHolder.line1.setText(personne.getNom() + " " + personne.getPrenom());
        viewHolder.line2.setText("TAG :" + personne.getNumero_tag());



        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}