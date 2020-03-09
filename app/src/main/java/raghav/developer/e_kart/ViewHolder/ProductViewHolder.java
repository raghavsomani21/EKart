package raghav.developer.e_kart.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import raghav.developer.e_kart.Interface.ItemClickListener;
import raghav.developer.e_kart.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  public TextView prod_Name,prod_Desc,prod_Price;
  public ImageView prod_Img;
  public ItemClickListener listener;

  public ProductViewHolder(@NonNull View itemView) {
    super(itemView);

    prod_Img = itemView.findViewById(R.id.prod_img);
    prod_Name = itemView.findViewById(R.id.prod_name);
    prod_Desc = itemView.findViewById(R.id.prod_desc);
    prod_Price = itemView.findViewById(R.id.prod_price);


  }

  public void setItemClickListener(ItemClickListener listener){
    this.listener = listener;
  }

  @Override
  public void onClick(View view) {
    listener.onCLick(view,getAdapterPosition(),false);
  }
}
