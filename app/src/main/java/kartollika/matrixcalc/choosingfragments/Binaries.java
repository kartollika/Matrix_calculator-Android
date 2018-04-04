package kartollika.matrixcalc.choosingfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import kartollika.matrixcalc.R;

public class Binaries extends Fragment {

    public Binaries() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_operation_fragment, container, false);

        int operation = getActivity().getIntent().getIntExtra("operation", -1);
        String[] strings = getActivity().getResources().getStringArray(R.array.binaryOperations);

        for (int i = 0; i < 2; ++i) {
            LinearLayout linearLayout;
            if (i == 0) {
                linearLayout = (LinearLayout) view.findViewById(R.id.ll0);
            } else {
                linearLayout = (LinearLayout) view.findViewById(R.id.ll1);
            }
            for (int j = 0; j < 4; ++j) {
                Button button = (Button) inflater.inflate(R.layout.item_choose_oper, container, false);
                int id = i * 4 + j;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button) v;
                        Intent intent = new Intent();
                        intent.putExtra("operation", v.getId());
                        String s = String.valueOf(btn.getText());
                        intent.putExtra("text", s);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
                if (id == operation) {
                    button.setBackgroundColor(getResources().getColor(R.color.colorOperationChoosed));
                }
                button.setId(id);

                button.setText(Html.fromHtml(strings[id]));
                linearLayout.addView(button);
            }
        }

        return view;
    }
}
