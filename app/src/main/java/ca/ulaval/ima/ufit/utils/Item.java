package ca.ulaval.ima.ufit.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

  private String username;
  private String title;
  private String description;
  private String brand;
  private String image;
  private int calories;

  public Item(String username, String title, String description, String brand, String image, int calories) {
    this.username = username;
    this.title = title;
    this.description = description;
    this.brand = brand;
    this.image = image;
    this.calories = calories;
  }

  protected Item(Parcel in) {
    username = in.readString();
    title = in.readString();
    description = in.readString();
    brand = in.readString();
    image = in.readString();
    calories = in.readInt();
  }

  public static final Creator<Item> CREATOR = new Creator<Item>() {
    @Override
    public Item createFromParcel(Parcel in) {
      return new Item(in);
    }

    @Override
    public Item[] newArray(int size) {
      return new Item[size];
    }
  };

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getCalories() {
    return calories;
  }

  public void setCalories(int calories) {
    this.calories = calories;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(username);
    dest.writeString(title);
    dest.writeString(description);
    dest.writeString(brand);
    dest.writeString(image);
    dest.writeInt(calories);
  }
}
