package kartollika.matrixcalc.choosingfragments;

import android.annotation.SuppressLint;
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

public class Unaries extends Fragment {

    public Unaries() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_operation_fragment, container, false);

        int operation = getActivity().getIntent().getIntExtra("operation", -1);
        final String[] strings = getActivity().getResources().getStringArray(R.array.unaryOperations);

        for (int i = 0; i < 2; ++i) {
            LinearLayout linearLayout;
            if (i == 0) {
                linearLayout = (LinearLayout) view.findViewById(R.id.ll0);
            } else {
                linearLayout = view.findViewById(R.id.ll1);
            }

            for (int j = 0; j < 4; ++j) {
                Button button = (Button) inflater.inflate(R.layout.item_choose_oper, container, false);
                button.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("operation", v.getId());
                        intent.putExtra("text", strings[v.getId() - 8]);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
                int id = 8 + i * 4 + j;
                if (id == operation) {
                    button.setBackgroundColor(getResources().getColor(R.color.colorOperationChoosed));
                }
                button.setId(id);

                button.setText(Html.fromHtml(strings[id - 8]));
                linearLayout.addView(button);
            }
        }
        return view;
    }
}
