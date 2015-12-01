package wipraktikum.informationwallandroidapp.BlackBoard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Remi on 26.11.2015.
 */
public class LayoutSelectionFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    private ImageView ivLayoutPreview = null;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static LayoutSelectionFragment create(int pageNumber) {
        LayoutSelectionFragment fragment = new LayoutSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LayoutSelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_layout_slide, container, false);

        initView(rootView);
        setDrawableByPageNumber();

        return rootView;
    }

    private void initView(ViewGroup rootView) {
        ivLayoutPreview = (ImageView) rootView.findViewById(R.id.blackboard_layout_selection_ivLayoutPreview);
    }

    private void setDrawableByPageNumber() {
        switch (getPageNumber()){
            case 0:
                ivLayoutPreview.setImageResource(R.drawable.text_only);
                break;
            case 1:
                ivLayoutPreview.setImageResource(R.drawable.document_view);
                break;
            case 2:
                ivLayoutPreview.setImageResource(R.drawable.document_and_info);
                break;
        }
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }


}

