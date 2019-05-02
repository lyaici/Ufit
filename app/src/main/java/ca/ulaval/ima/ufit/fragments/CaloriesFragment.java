package ca.ulaval.ima.ufit.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ca.ulaval.ima.ufit.R;
import ca.ulaval.ima.ufit.utils.Item;
import ca.ulaval.ima.ufit.utils.ParcelableItemList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaloriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaloriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaloriesFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_ITEMLIST = "itemList";
  private int totalCalories;

  // TODO: Rename and change types of parameters

  private OnFragmentInteractionListener mListener;
  private ParcelableItemList mItemList;
  private TextView calories;

  public CaloriesFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment CaloriesFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static CaloriesFragment newInstance(ParcelableItemList items) {
    CaloriesFragment fragment = new CaloriesFragment();
    Bundle args = new Bundle();
    args.putParcelable(ARG_ITEMLIST, items);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mItemList = getArguments().getParcelable(ARG_ITEMLIST);
    }
    for (Item item : mItemList.getList()) {
      totalCalories = totalCalories + item.getCalories();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_calories, container, false);
    calories = (TextView) view.findViewById(R.id.calories);
    calories.setText(Integer.toString(totalCalories) + " Kcal");
    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  public void addToItemList(Item item) {
    try {
      mItemList.add(item);
      totalCalories = 0;
      for (Item iterable : mItemList.getList()) {
        totalCalories = totalCalories + iterable.getCalories();
      }
      calories.setText(Integer.toString(totalCalories) + " Kcal");
    } catch (NullPointerException e) {
      System.err.println(e);
    }
  }


  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
