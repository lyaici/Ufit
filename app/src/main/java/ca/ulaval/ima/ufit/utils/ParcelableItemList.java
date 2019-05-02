package ca.ulaval.ima.ufit.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ParcelableItemList implements Parcelable {

  private ArrayList<Item> itemArrayList;

  public ParcelableItemList() {
    this.itemArrayList = new ArrayList<>();
  }

  protected ParcelableItemList(Parcel in) {
    itemArrayList = in.createTypedArrayList(Item.CREATOR);
  }

  public static final Creator<ParcelableItemList> CREATOR = new Creator<ParcelableItemList>() {
    @Override
    public ParcelableItemList createFromParcel(Parcel in) {
      return new ParcelableItemList(in);
    }

    @Override
    public ParcelableItemList[] newArray(int size) {
      return new ParcelableItemList[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(itemArrayList);
  }

  public ArrayList<Item> getList() {
    return itemArrayList;
  }

  public void add(Item item) {
    this.itemArrayList.add(item);
  }
}
