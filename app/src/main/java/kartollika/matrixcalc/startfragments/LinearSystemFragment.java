package kartollika.matrixcalc.startfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kartollika.matrixcalc.App;
import kartollika.matrixcalc.InputMatrixActivity;
import kartollika.matrixcalc.MainActivity;
import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.ShowResultActivity;

import static android.app.Activity.RESULT_OK;

public class LinearSystemFragment extends Fragment implements View.OnClickListener {

    private static final int MATRIX_SYSTEM = 0;
    private static final int SOLVE = 1;

    Matrix matrix;

    public LinearSystemFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linear_system, container, false);

        Button system = view.findViewById(R.id.system);
        Button solveSystem = view.findViewById(R.id.solve_system);

        system.setOnClickListener(this);
        solveSystem.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();
        Intent intent;

        switch (ID) {
            case R.id.system:
                intent = new Intent(getContext(), InputMatrixActivity.class);
                intent.putExtra("matrixSys", new Matrix(matrix));
                startActivityForResult(intent, MATRIX_SYSTEM);
                break;

            case R.id.solve_system:
                intent = new Intent(getContext(), ShowResultActivity.class);
                intent.putExtra("matrix", new Matrix(matrix));
                intent.putExtra("operation", 16);
                startActivityForResult(intent, SOLVE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = ((MainActivity) getActivity()).getPrefs();
        int defDimN = Integer.parseInt(preferences.getString("defDimN", "3"));
        int defDimM = Integer.parseInt(preferences.getString("defDimM", "3"));

        matrix = App.systemMatrix;

        if (matrix == null) {
            matrix = new Matrix(null, null, defDimN, defDimM);
        }

        if (!matrix.isEdited()) {
            matrix = new Matrix(null, null, defDimN, defDimM);
        }

        String sA = "<big>A</big><sup><small>" + String.valueOf(matrix.getRowCount())
                + "x" + String.valueOf(matrix.getColumnCount()) + "</small></sup>";

        ((Button) getActivity().findViewById(R.id.system)).setText(Html.fromHtml(sA));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        App.systemMatrix = matrix;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            if (requestCode != SOLVE) {
                return;
            }
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MATRIX_SYSTEM:
                    if (data.getExtras() != null) {
                        Matrix pmatrix = data.getExtras().getParcelable("matrix");
                        if (pmatrix != null) {
                            matrix = pmatrix;
                            matrix.setEdited();
                        } else {
                            return;
                        }
                        App.systemMatrix = matrix;
                    }
                    break;

                case SOLVE:
                    OperationsFragment.afterSolveOperations(getActivity());
                    /*AppRater.appLaunched(getContext(), getActivity().getFragmentManager());
                    InterstitialShow.showInterstitialAd();*/
            }
        }
    }
}
